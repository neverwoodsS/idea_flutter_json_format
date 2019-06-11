### Update
#### ver 2.1

generate 『toJson』 function to support converting object to map

```json
{
  "mobile" : "17689209851",
  "avatar" : "http:\/\/album.test.getqood.com\/images\/file\/D9360020804F73C96004009503B3F72387583552.PNG",
  "organization__id" : "1197146792697790465",
  "birthdate" : "2018-12-13T00:00:00.000+0800",
  "type" : "普通会员",
  "apply_time" : "2011-08-23T00:00:00.000+0800",
  "nick_name" : "学员测试",
  "id" : "1139173367484514632",
  "course_title" : "1",
  "created_at" : "2018-12-14T09:48:43.000+0800",
  "updated_at" : "2018-12-25T18:56:31.000+0800",
  "organization_name" : "罗",
  "gender" : "男"
}
```

```dart
class Test {
  String mobile;
  String avatar;
  String organizationId;
  String birthdate;
  String type;
  String applyTime;
  String nickName;
  String id;
  String courseTitle;
  String createdAt;
  String updatedAt;
  String organizationName;
  String gender;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.mobile = map['mobile'];
    testBean.avatar = map['avatar'];
    testBean.organizationId = map['organization__id'];
    testBean.birthdate = map['birthdate'];
    testBean.type = map['type'];
    testBean.applyTime = map['apply_time'];
    testBean.nickName = map['nick_name'];
    testBean.id = map['id'];
    testBean.courseTitle = map['course_title'];
    testBean.createdAt = map['created_at'];
    testBean.updatedAt = map['updated_at'];
    testBean.organizationName = map['organization_name'];
    testBean.gender = map['gender'];
    return testBean;
  }

  Map toJson() => {
    "mobile": mobile,
    "avatar": avatar,
    "organization__id": organizationId,
    "birthdate": birthdate,
    "type": type,
    "apply_time": applyTime,
    "nick_name": nickName,
    "id": id,
    "course_title": courseTitle,
    "created_at": createdAt,
    "updated_at": updatedAt,
    "organization_name": organizationName,
    "gender": gender,
  };
}
```

```dart
import 'dart:convert'

var json = JsonEncoder().convert(obj);
```

### Install
#### 1. Download jar
https://plugins.jetbrains.com/plugin/11551-dart-json-format

#### 2. Search in IDE

Plugins -> Browse repositories -> input "dart_json_format"

### Generate

![gif](example_image/jsonformat.gif)

### Examples

#### 1. Simple data

```json
{
  "name": "zll",
  "age": 29,
  "star": 4.5,
  "married": true
}
```

```dart
class Test {
  String name;
  int age;
  double star;
  bool married;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.name = map['name'];
    testBean.age = map['age'];
    testBean.star = map['star'];
    testBean.married = map['married'];
    return testBean;
  }
}
```

#### 2. Ojbect

```json
{
  "programmer": {
    "name": "zll",
    "age": 29,
    "star": 4.5,
    "married": true
  }
}
```

```dart
class Test {
  ProgrammerBean programmer;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.programmer = ProgrammerBean.fromMap(map['programmer']);
    return testBean;
  }
}

class ProgrammerBean {
  String name;
  int age;
  double star;
  bool married;

  static ProgrammerBean fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    ProgrammerBean programmerBean = ProgrammerBean();
    programmerBean.name = map['name'];
    programmerBean.age = map['age'];
    programmerBean.star = map['star'];
    programmerBean.married = map['married'];
    return programmerBean;
  }
}
```

#### 3. Array

```json
{
  "names": ["zll", "kfc"],
  "ages": [29, 25],
  "stars": [4.5, 4.4],
  "marrieds": [true, false]
}
```

```dart
class Test {
  List<String> names;
  List<int> ages;
  List<double> stars;
  List<bool> marrieds;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.names = List()..addAll(
      (map['names'] as List ?? []).map((o) => o.toString())
    );
    testBean.ages = List()..addAll(
      (map['ages'] as List ?? []).map((o) => int.tryParse(o.toString()))
    );
    testBean.stars = List()..addAll(
      (map['stars'] as List ?? []).map((o) => double.tryParse(o.toString()))
    );
    testBean.marrieds = List()..addAll(
      (map['marrieds'] as List ?? []).map((o) => o.toString() == 'true')
    );
    return testBean;
  }
}
```

#### 4. Array of Object
```json
{
  "programmers": [
    {
      "name": "zll",
      "age": 29,
      "star": 4.5,
      "married": true
    },{
      "name": "kfc",
      "age": 25,
      "star": 4.1,
      "married": false
    }
  ]
}
```

```dart
class Test {
  List<ProgrammersBean> programmers;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.programmers = List()..addAll(
      (map['programmers'] as List ?? []).map((o) => ProgrammersBean.fromMap(o))
    );
    return testBean;
  }
}

class ProgrammersBean {
  String name;
  int age;
  double star;
  bool married;

  static ProgrammersBean fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    ProgrammersBean programmersBean = ProgrammersBean();
    programmersBean.name = map['name'];
    programmersBean.age = map['age'];
    programmersBean.star = map['star'];
    programmersBean.married = map['married'];
    return programmersBean;
  }
}
```

#### 5. Nested Array
```json
{
  "something": [[[[[1]]]]]
}
```

```dart
class Test {
  List<List<List<List<List<int>>>>> something;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.something = List()..addAll(
      (map['something'] as List ?? []).map((o) => List()..addAll((o as List ?? []).map((oo) => List()..addAll((oo as List ?? []).map((ooo) => List()..addAll((ooo as List ?? []).map((oooo) => List()..addAll((oooo as List ?? []).map((ooooo) => int.tryParse(ooooo.toString()))))))))))
    );
    return testBean;
  }
}
```

#### 5. Nested Array of Object
```json
{
  "something": [[[[[{
    "name": "zll",
    "age": 29,
    "star": 4.5,
    "married": true
  }]]]]]
}
```

```dart
class Test {
  List<List<List<List<List<SomethingBean>>>>> something;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.something = List()..addAll(
      (map['something'] as List ?? []).map((o) => List()..addAll((o as List ?? []).map((oo) => List()..addAll((oo as List ?? []).map((ooo) => List()..addAll((ooo as List ?? []).map((oooo) => List()..addAll((oooo as List ?? []).map((ooooo) => SomethingBean.fromMap(ooooo))))))))))
    );
    return testBean;
  }
}

class SomethingBean {
  String name;
  int age;
  double star;
  bool married;

  static SomethingBean fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    SomethingBean somethingBean = SomethingBean();
    somethingBean.name = map['name'];
    somethingBean.age = map['age'];
    somethingBean.star = map['star'];
    somethingBean.married = map['married'];
    return somethingBean;
  }
}
```

#### 6. Empty or Null
```json
{
  "obj": null,
  "emptyList": [],
  "nullList": [null]
}
```

```dart
class Test {
  dynamic obj;
  List<dynamic> emptyList;
  List<dynamic> nullList;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.obj = map['obj'];
    testBean.emptyList = map['emptyList'];
    testBean.nullList = map['nullList'];
    return testBean;
  }
}
```

#### 7. Root Array
```json
[
  {
    "name": "zll",
    "age": 29,
    "star": 4.5,
    "married": true
  },
  {
	"name": "kfc",
	"age": 25,
	"star": 4.1,
	"married": false
  }
]
```

##### Take only array[0] to use

```dart
class Test {
  String name;
  int age;
  double star;
  bool married;

  static Test fromMap(Map<String, dynamic> map) {
    if (map == null) return null;
    Test testBean = Test();
    testBean.name = map['name'];
    testBean.age = map['age'];
    testBean.star = map['star'];
    testBean.married = map['married'];
    return testBean;
  }
}
```

