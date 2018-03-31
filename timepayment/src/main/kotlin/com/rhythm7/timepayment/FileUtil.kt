package com.rhythm7.timepayment

import java.io.File
import java.io.FileNotFoundException

/**
 * Created by Jaminchanks on 2018-03-31.
 */
@Throws(FileNotFoundException::class, IllegalArgumentException::class)
fun File.eachFileRecurse(func: (File) -> Unit) {
    checkDir(this)
    val files = this.listFiles()
    if (files != null) {
        val size = files.size

        for (i in 0 until size) {
            val file = files[i]
            if (file.isDirectory) {
                func(file)
                file.eachFileRecurse(func)
            } else {
                func(file)
            }
        }

    }
}

@Throws(FileNotFoundException::class, IllegalArgumentException::class)
private fun checkDir(dir: File) {
    if (!dir.exists()) {
        throw FileNotFoundException(dir.absolutePath)
    } else if (!dir.isDirectory) {
        throw IllegalArgumentException("The provided File object is not a directory: " + dir.absolutePath)
    }
}