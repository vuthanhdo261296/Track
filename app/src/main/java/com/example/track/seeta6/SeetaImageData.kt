package com.example.track.seeta6

class SeetaImageData {
    var data: ByteArray
    var width: Int
    var height: Int
    var channels: Int

    constructor(width: Int, height: Int, channels: Int) {
        data = ByteArray(width * height * channels)
        this.width = width
        this.height = height
        this.channels = channels
    }

    constructor(width: Int, height: Int) {
        data = ByteArray(width * height * 1)
        this.width = width
        this.height = height
        this.channels = 1
    }
}


