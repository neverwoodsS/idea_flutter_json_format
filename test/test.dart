import 'dart:convert';
import 'temp.dart';
import 'dart:io';

main() async {
  var json = await File('test.json').readAsString();
  var map = JsonDecoder().convert(json);

  Temp temp = Temp.fromMap(map);
  print(temp.data.expressAddress.receiverCellphone);
  print(temp.emptyList);
  print(temp.nullList);
  print(temp.nullObj);
}