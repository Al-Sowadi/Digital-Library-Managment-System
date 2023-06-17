package com.example.digitallibraryhub

class BooksInfo(BName:String,AName:String,DBook:String,Tbook:String,Burl:String,Burl2:String,bookId:Int,UTime:String) {
    val BBName: String
    val AAName: String
    val BBurl: String
    val BBurl2: String
    val DDBook :String
    val TTbook:String
    val BookId:Int
    val uTime:String
    init {
    }

    init {
        BBName = BName
        AAName = AName
        BBurl = Burl
        BBurl2 = Burl2
        DDBook= DBook
        TTbook= Tbook
        BookId =bookId
        uTime=UTime
    }
}