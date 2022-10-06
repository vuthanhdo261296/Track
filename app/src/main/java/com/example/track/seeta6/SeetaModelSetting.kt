package com.example.track.seeta6

class SeetaModelSetting {

    var id: Int // when device is GPU, id means GPU id
    var model: Array<String?>
    var device: SeetaDevice

    constructor(id: Int, models: Array<String>, dev: SeetaDevice) {
        this.id = id
        this.device = dev
        this.model = arrayOfNulls(models.size)
        for (i in models.indices) {
            model[i] = models[i]
        }
    }
}
