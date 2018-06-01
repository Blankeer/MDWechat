package com.blanke.mdwechat

import com.blanke.mdwechat.bean.ViewTreeItem

object ViewTreeRepo {
    val ConversationListViewItem: ViewTreeItem by lazy {
        ViewTreeItem(
                CC.LinearLayout.name,
                arrayOf(
                        ViewTreeItem(
                                CC.RelativeLayout.name,
                                arrayOf(
                                        ViewTreeItem(CC.ImageView.name),
                                        ViewTreeItem(CC.TextView.name),
                                        ViewTreeItem(CC.ImageView.name)
                                )
                        ),
                        ViewTreeItem(
                                CC.LinearLayout.name,
                                arrayOf(
                                        ViewTreeItem(
                                                CC.LinearLayout.name,
                                                arrayOf(
                                                        ViewTreeItem(
                                                                CC.LinearLayout.name,
                                                                arrayOf(
                                                                        ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                )
                                                        ),
                                                        ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                )
                                        ),
                                        ViewTreeItem(
                                                CC.LinearLayout.name,
                                                arrayOf(
                                                        ViewTreeItem(
                                                                CC.LinearLayout.name,
                                                                arrayOf(
                                                                        ViewTreeItem(CC.ImageView.name),
                                                                        ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                )
                                                        ),
                                                        ViewTreeItem(
                                                                CC.LinearLayout.name,
                                                                arrayOf(
                                                                        ViewTreeItem(CC.ImageView.name),
                                                                        ViewTreeItem(CC.ImageView.name),
                                                                        ViewTreeItem(CC.ImageView.name),
                                                                        ViewTreeItem(CC.ImageView.name)
                                                                )
                                                        )

                                                )
                                        )
                                )
                        )
                )
        )
    }
}