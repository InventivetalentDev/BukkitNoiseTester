package org.inventivetalent.noisetester

import tornadofx.*


class MyApp: App(MyView::class, Styles::class) {
    override val primaryView = MyView::class

    init {
        println("init")
        reloadStylesheetsOnFocus()
    }

}