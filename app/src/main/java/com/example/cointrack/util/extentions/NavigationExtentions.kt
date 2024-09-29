package com.example.cointrack.util.extentions

fun String.addArgs(vararg args: String): String {

    val route = this

    return buildString {

        append(route)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}