class Temp {
  String code;
  String message;
  bool success;
  dynamic emptyList;
  dynamic nullObj;
  DataBean data;
  ExtraDataBean extraData;
  List<bool> boolList;
  List<double> doubleList;
  List<int> intList;
  List<TestFromListListBean> testFromList;
  List<dynamic> nullList;

  static Temp fromMap(Map<String, dynamic> map) {
    Temp test = new Temp();
    test.code = map['code'];
    test.message = map['message'];
    test.success = map['success'];
    test.emptyList = map['emptyList'];
    test.nullObj = map['nullObj'];
    test.nullList = map['nullList'];
    test.data = DataBean.fromMap(map['data']);
    test.extraData = ExtraDataBean.fromMap(map['extraData']);
    test.testFromList = TestFromListListBean.fromMapList(map['testFromList']);

    List<dynamic> dynamicList0 = map['boolList'] ?? [];
    test.boolList = new List();
    test.boolList.addAll(dynamicList0.map((o) => o.toString() == 'true'));

    List<dynamic> dynamicList1 = map['doubleList'] ?? [];
    test.doubleList = new List();
    test.doubleList.addAll(dynamicList1.map((o) => double.parse(o.toString())));

    List<dynamic> dynamicList2 = map['intList'] ?? [];
    test.intList = new List();
    test.intList.addAll(dynamicList2.map((o) => int.parse(o.toString())));

    return test;
  }

  static List<Temp> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
    List<Temp> list = new List(mapList.length);
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

  static DataBean fromMap(Map<String, dynamic> map) {
    DataBean dataBean = new DataBean();
    dataBean.expressAddress = ExpressAddressBean.fromMap(map['expressAddress']);
    dataBean.facetofaceAddress = FacetofaceAddressBean.fromMap(map['facetofaceAddress']);
    dataBean.order = OrderBean.fromMap(map['order']);
    return dataBean;
  }

  static List<DataBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
    List<DataBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class ExtraDataBean {

  static ExtraDataBean fromMap(Map<String, dynamic> map) {
    ExtraDataBean extraDataBean = new ExtraDataBean();
    return extraDataBean;
  }

  static List<ExtraDataBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
    List<ExtraDataBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}

class TestFromListListBean {
  String testKey;

  static TestFromListListBean fromMap(Map<String, dynamic> map) {
    TestFromListListBean testFromListListBean = new TestFromListListBean();
    testFromListListBean.testKey = map['testKey'];
    return testFromListListBean;
  }

  static List<TestFromListListBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
    List<TestFromListListBean> list = new List(mapList.length);
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
    ExpressAddressBean expressAddressBean = new ExpressAddressBean();
    expressAddressBean.address = map['address'];
    expressAddressBean.createdDatetime = map['createdDatetime'];
    expressAddressBean.receiver = map['receiver'];
    expressAddressBean.receiverCellphone = map['receiverCellphone'];
    expressAddressBean.sid = map['sid'];
    expressAddressBean.title = map['title'];
    expressAddressBean.type = map['type'];
    expressAddressBean.userSid = map['userSid'];
    expressAddressBean.sortNumber = map['sortNumber'];
    return expressAddressBean;
  }

  static List<ExpressAddressBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
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
    FacetofaceAddressBean facetofaceAddressBean = new FacetofaceAddressBean();
    facetofaceAddressBean.address = map['address'];
    facetofaceAddressBean.receiver = map['receiver'];
    facetofaceAddressBean.receiverCellphone = map['receiverCellphone'];
    facetofaceAddressBean.sid = map['sid'];
    facetofaceAddressBean.title = map['title'];
    facetofaceAddressBean.type = map['type'];
    facetofaceAddressBean.userSid = map['userSid'];
    facetofaceAddressBean.sortNumber = map['sortNumber'];
    return facetofaceAddressBean;
  }

  static List<FacetofaceAddressBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
    List<FacetofaceAddressBean> list = new List(mapList.length);
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
  bool isDelete;
  bool isSequential;
  double brokerStars;
  int bidding;
  int brokerDealNum;
  int deliveryFee;
  int evaluateStarts;
  int ticketPrice;
  int ticketQuantity;
  int totalPrice;
  List<OrderStatusArrayListBean> orderStatusArray;

  static OrderBean fromMap(Map<String, dynamic> map) {
    OrderBean orderBean = new OrderBean();
    orderBean.brokerAvatar = map['brokerAvatar'];
    orderBean.brokerCellphone = map['brokerCellphone'];
    orderBean.brokerName = map['brokerName'];
    orderBean.brokerSid = map['brokerSid'];
    orderBean.code = map['code'];
    orderBean.cover = map['cover'];
    orderBean.createdDatetime = map['createdDatetime'];
    orderBean.deliveryAddressSid = map['deliveryAddressSid'];
    orderBean.orderStatus = map['orderStatus'];
    orderBean.orderStatusDesp = map['orderStatusDesp'];
    orderBean.orderType = map['orderType'];
    orderBean.payType = map['payType'];
    orderBean.postTicketSid = map['postTicketSid'];
    orderBean.receiveDatetime = map['receiveDatetime'];
    orderBean.receiver = map['receiver'];
    orderBean.receiverAddress = map['receiverAddress'];
    orderBean.receiverCellphone = map['receiverCellphone'];
    orderBean.receiverTitle = map['receiverTitle'];
    orderBean.remark = map['remark'];
    orderBean.requestDatetime = map['requestDatetime'];
    orderBean.showName = map['showName'];
    orderBean.showSchedule = map['showSchedule'];
    orderBean.showScheduleSid = map['showScheduleSid'];
    orderBean.showSid = map['showSid'];
    orderBean.sid = map['sid'];
    orderBean.stateDesp = map['stateDesp'];
    orderBean.ticketSid = map['ticketSid'];
    orderBean.tradeType = map['tradeType'];
    orderBean.userCellphone = map['userCellphone'];
    orderBean.userLeaveMessage = map['userLeaveMessage'];
    orderBean.userSid = map['userSid'];
    orderBean.venueAddress = map['venueAddress'];
    orderBean.venueName = map['venueName'];
    orderBean.isDelete = map['isDelete'];
    orderBean.isSequential = map['isSequential'];
    orderBean.brokerStars = map['brokerStars'];
    orderBean.bidding = map['bidding'];
    orderBean.brokerDealNum = map['brokerDealNum'];
    orderBean.deliveryFee = map['deliveryFee'];
    orderBean.evaluateStarts = map['evaluateStarts'];
    orderBean.ticketPrice = map['ticketPrice'];
    orderBean.ticketQuantity = map['ticketQuantity'];
    orderBean.totalPrice = map['totalPrice'];
    orderBean.orderStatusArray = OrderStatusArrayListBean.fromMapList(map['orderStatusArray']);
    return orderBean;
  }

  static List<OrderBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
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
    OrderStatusArrayListBean orderStatusArrayListBean = new OrderStatusArrayListBean();
    orderStatusArrayListBean.operateDatetime = map['operateDatetime'];
    orderStatusArrayListBean.operateUserSid = map['operateUserSid'];
    orderStatusArrayListBean.operateUsername = map['operateUsername'];
    orderStatusArrayListBean.orderSid = map['orderSid'];
    orderStatusArrayListBean.orderType = map['orderType'];
    orderStatusArrayListBean.sid = map['sid'];
    orderStatusArrayListBean.state = map['state'];
    return orderStatusArrayListBean;
  }

  static List<OrderStatusArrayListBean> fromMapList(dynamic mapList) {
    if (mapList == null) return [];
    List<OrderStatusArrayListBean> list = new List(mapList.length);
    for (int i = 0; i < mapList.length; i++) {
      list[i] = fromMap(mapList[i]);
    }
    return list;
  }
}
