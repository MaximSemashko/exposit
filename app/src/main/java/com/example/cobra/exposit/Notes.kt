package com.example.cobra.exposit

class Notes {
    var name: String? = null

    var date: String? = null

    var userId: String? = null

    constructor() {

    }

    constructor(name: String, date: String) {
        this.name = name
        this.date = date
    }

    constructor(name: String, date: String, userId: String) {
        this.name = name
        this.date = date
        this.userId = userId
    }
}
