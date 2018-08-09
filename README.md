## how to use
#### 1. download jar (or download source code and build it yourself)

![jar](example_image/jar.jpg)

#### 2. install -> install plugin from disk...

![install](example_image/install.jpg)

#### 3. create a new dart file, then press command + N (mac os)

![file](example_image/file.jpg)

#### 4. choose "flutter json format", then put your json in

json:

```
{
	"code": "2000",
	"data": {
		"expressAddress": {
			"address": "控江路1209",
			"createdDatetime": "2015-12-09 19:36:38",
			"receiver": "张**",
			"receiverCellphone": "1502379****",
			"sid": "c25c1954ca204dee8fc18f51bcc71a3e",
			"sortNumber": 1,
			"title": "先生",
			"type": "EXPRESS",
			"userSid": "a19cf6e3586143d283dd4128c456bfaf"
		},
		"facetofaceAddress": {
			"address": "",
			"receiver": "罗",
			"receiverCellphone": "1388345****",
			"sid": "001190e19e754001b53701d0aa81bfe0",
			"sortNumber": 1,
			"title": "女士",
			"type": "FACETOFACE",
			"userSid": "a19cf6e3586143d283dd4128c456bfaf"
		},
		"order": {
			"bidding": 2300,
			"brokerAvatar": "broker/getAvatar?key=avatar/b82378ccaccb403c9d8420274372c904",
			"brokerCellphone": "1347282****",
			"brokerDealNum": 15,
			"brokerName": "王**",
			"brokerSid": "b82378ccaccb403c9d8420274372c904",
			"brokerStars": 4.5,
			"code": "1512151307270113",
			"cover": "show/getPoster?key=52f30bbce4ef4122919cbc95c2f01c36/52f30bbce4ef4122919cbc95c2f01c36",
			"createdDatetime": "2015-12-15 13:07:28",
			"deliveryAddressSid": "c25c1954ca204dee8fc18f51bcc71a3e",
			"deliveryFee": 0,
			"evaluateStarts": 0,
			"isDelete": false,
			"isSequential": false,
			"orderStatus": "CLOSED",
			"orderStatusArray": [{
				"operateDatetime": "2015-12-15 13:07:28",
				"operateUserSid": "b82378ccaccb403c9d8420274372c904",
				"operateUsername": "王**",
				"orderSid": "21bc3cc65e9e47af952c1f4f1f0fd85a",
				"orderType": "1",
				"sid": "04940ed81540466ea4408f79989a5d54",
				"state": "TAKING"
			}],
			"orderStatusDesp": "",
			"orderType": "1",
			"payType": "",
			"postTicketSid": "65cc6d54300349e984134ecd0faf3ede",
			"receiveDatetime": "2015-12-15 13:07:28",
			"receiver": "张**",
			"receiverAddress": "控江路1209",
			"receiverCellphone": "1502379****",
			"receiverTitle": "先生",
			"remark": "",
			"requestDatetime": "2015-12-15 13:07:28",
			"showName": "Love Radio 品冠 现在你在哪里 巡回演唱会上海站",
			"showSchedule": "2016-01-09 19:30:00",
			"showScheduleSid": "eecfd0657fb445a7a36abedc9b621c89",
			"showSid": "52f30bbce4ef4122919cbc95c2f01c36",
			"sid": "21bc3cc65e9e47af952c1f4f1f0fd85a",
			"stateDesp": "已关闭",
			"ticketPrice": 88000,
			"ticketQuantity": 1,
			"ticketSid": "c4583aa8a79a478e8e5cd14691028430",
			"totalPrice": 2300,
			"tradeType": "EXPRESS",
			"userCellphone": "1502379****",
			"userLeaveMessage": "",
			"userSid": "a19cf6e3586143d283dd4128c456bfaf",
			"venueAddress": "上海市长宁区武夷路777号",
			"venueName": "上海国际体操中心"
		}
	},
	"extraData": {},
	"message": "",
	"success": true
}
```
![plugin](example_image/plugin.jpg)

#### 5. click "ok", then close plugin, and paste the code to your file

result codes

```
class Temp {
  String code;
  String message;
  bool success;
  DataBean data;
  ExtraDataBean extraData;

  static Temp fromMap(Map<String, dynamic> map) {
    return new Temp()
      ..code = map['code']
      ..message = map['message']
      ..success = map['success']
      ..data = DataBean.fromMap(map['data'])
      ..extraData = ExtraDataBean.fromMap(map['extraData']);
  }

  static List<Temp> fromMapList(dynamic mapList) {
    List<Temp> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }

}

class ExtraDataBean {

  static ExtraDataBean fromMap(Map<String, dynamic> map) {
    return new ExtraDataBean();
  }

  static List<ExtraDataBean> fromMapList(dynamic mapList) {
    List<ExtraDataBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class OrderBean {
  String brokerAvatar;
  String brokerCellphone;
  String brokerName;
  String brokerSid;
  String code;
  String cover;
  String createdDatetime;
  String deliveryAddressSid;
  String orderStatus;
  String orderStatusDesp;
  String orderType;
  String payType;
  String postTicketSid;
  String receiveDatetime;
  String receiver;
  String receiverAddress;
  String receiverCellphone;
  String receiverTitle;
  String remark;
  String requestDatetime;
  String showName;
  String showSchedule;
  String showScheduleSid;
  String showSid;
  String sid;
  String stateDesp;
  String ticketSid;
  String tradeType;
  String userCellphone;
  String userLeaveMessage;
  String userSid;
  String venueAddress;
  String venueName;
  int bidding;
  int brokerDealNum;
  int deliveryFee;
  int evaluateStarts;
  int ticketPrice;
  int ticketQuantity;
  int totalPrice;
  double brokerStars;
  bool isDelete;
  bool isSequential;
  List<OrderStatusArrayListBean> orderStatusArray;

  static OrderBean fromMap(Map<String, dynamic> map) {
    return new OrderBean()
      ..brokerAvatar = map['brokerAvatar']
      ..brokerCellphone = map['brokerCellphone']
      ..brokerName = map['brokerName']
      ..brokerSid = map['brokerSid']
      ..code = map['code']
      ..cover = map['cover']
      ..createdDatetime = map['createdDatetime']
      ..deliveryAddressSid = map['deliveryAddressSid']
      ..orderStatus = map['orderStatus']
      ..orderStatusDesp = map['orderStatusDesp']
      ..orderType = map['orderType']
      ..payType = map['payType']
      ..postTicketSid = map['postTicketSid']
      ..receiveDatetime = map['receiveDatetime']
      ..receiver = map['receiver']
      ..receiverAddress = map['receiverAddress']
      ..receiverCellphone = map['receiverCellphone']
      ..receiverTitle = map['receiverTitle']
      ..remark = map['remark']
      ..requestDatetime = map['requestDatetime']
      ..showName = map['showName']
      ..showSchedule = map['showSchedule']
      ..showScheduleSid = map['showScheduleSid']
      ..showSid = map['showSid']
      ..sid = map['sid']
      ..stateDesp = map['stateDesp']
      ..ticketSid = map['ticketSid']
      ..tradeType = map['tradeType']
      ..userCellphone = map['userCellphone']
      ..userLeaveMessage = map['userLeaveMessage']
      ..userSid = map['userSid']
      ..venueAddress = map['venueAddress']
      ..venueName = map['venueName']
      ..bidding = map['bidding']
      ..brokerDealNum = map['brokerDealNum']
      ..deliveryFee = map['deliveryFee']
      ..evaluateStarts = map['evaluateStarts']
      ..ticketPrice = map['ticketPrice']
      ..ticketQuantity = map['ticketQuantity']
      ..totalPrice = map['totalPrice']
      ..brokerStars = map['brokerStars']
      ..isDelete = map['isDelete']
      ..isSequential = map['isSequential']
      ..orderStatusArray = OrderStatusArrayListBean.fromMapList(map['orderStatusArray']);
  }

  static List<OrderBean> fromMapList(dynamic mapList) {
    List<OrderBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class OrderStatusArrayListBean {
  String operateDatetime;
  String operateUserSid;
  String operateUsername;
  String orderSid;
  String orderType;
  String sid;
  String state;

  static OrderStatusArrayListBean fromMap(Map<String, dynamic> map) {
    return new OrderStatusArrayListBean()
      ..operateDatetime = map['operateDatetime']
      ..operateUserSid = map['operateUserSid']
      ..operateUsername = map['operateUsername']
      ..orderSid = map['orderSid']
      ..orderType = map['orderType']
      ..sid = map['sid']
      ..state = map['state'];
  }

  static List<OrderStatusArrayListBean> fromMapList(dynamic mapList) {
    List<OrderStatusArrayListBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class DataBean {
  ExpressAddressBean expressAddress;
  FacetofaceAddressBean facetofaceAddress;
  OrderBean order;

  DataBean({
    this.expressAddress,
    this.facetofaceAddress,
    this.order,
  });

  static DataBean fromMap(Map<String, dynamic> map) {
    return new DataBean()
      ..expressAddress = ExpressAddressBean.fromMap(map['expressAddress'])
      ..facetofaceAddress = FacetofaceAddressBean.fromMap(map['facetofaceAddress'])
      ..order = OrderBean.fromMap(map['order']);
  }

  static List<DataBean> fromMapList(dynamic mapList) {
    List<DataBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class ExpressAddressBean {
  String address;
  String createdDatetime;
  String receiver;
  String receiverCellphone;
  String sid;
  String title;
  String type;
  String userSid;
  int sortNumber;

  static ExpressAddressBean fromMap(Map<String, dynamic> map) {
    return new ExpressAddressBean()
      ..address = map['address']
      ..createdDatetime = map['createdDatetime']
      ..receiver = map['receiver']
      ..receiverCellphone = map['receiverCellphone']
      ..sid = map['sid']
      ..title = map['title']
      ..type = map['type']
      ..userSid = map['userSid']
      ..sortNumber = map['sortNumber'];
  }

  static List<ExpressAddressBean> fromMapList(dynamic mapList) {
    List<ExpressAddressBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class FacetofaceAddressBean {
  String address;
  String receiver;
  String receiverCellphone;
  String sid;
  String title;
  String type;
  String userSid;
  int sortNumber;

  static FacetofaceAddressBean fromMap(Map<String, dynamic> map) {
    return new FacetofaceAddressBean()
      ..address = map['address']
      ..receiver = map['receiver']
      ..receiverCellphone = map['receiverCellphone']
      ..sid = map['sid']
      ..title = map['title']
      ..type = map['type']
      ..userSid = map['userSid']
      ..sortNumber = map['sortNumber'];
  }

  static List<FacetofaceAddressBean> fromMapList(dynamic mapList) {
    List<FacetofaceAddressBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

```


#### 6. use classes in code

```
main() {
  var map = new JsonDecoder().convert(json);
  Temp temp = Temp.fromMap(map);
  print(temp.data.expressAddress.address);
}
```

it prints:

```
控江路1209
```

#### 7. gif

![gif](example_image/jsonformat.gif)

