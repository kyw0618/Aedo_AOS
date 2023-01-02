package com.aedo.my_heaven.model.shop

data class MyOrder (
    val result : List<MyOrders>? = null
)

data class MyOrders(
    val place : String? = null,     // 장례식장
    val item : String? = null,      // 화환명
    val price : String? = null,     // 화환 가격
    val receiver_name : String? = null,     // 받는 사람
    val receiver_number : String? = null,   // 받는 사람 전화번호
    val sender_name : String? = null,       // 보내는 사람
    val sender_number : String? = null,     // 보내는 사람 전화번호
    val word : String? = null,      // 화환 리본 문구
    val company : String? = null,   // 회사명 또는 모임명
    val created : String? = null,   // 주문 생성일자
    val order_complete : Boolean? = null,    // 주문 완료 코드
    val merchant_uid: String? = null,       // 주문 번호
    val id : String? = null
)