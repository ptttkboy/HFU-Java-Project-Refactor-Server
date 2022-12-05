/* 12/5 js practice */
// make sure the safety of using strict mode;
"use strict";

/**
 * 確認刪除？
 */
// querySelector只會選到第一個，querySelectorAll則全部
const btn_delete = document.querySelectorAll(".btn-delete");

// 把所有刪除按鈕都加上事件
btn_delete.forEach( btn => {

    btn.addEventListener("click", (e) => {

        if (!confirm("確定要刪除餐廳嗎？")) {
            // 如果按取消，使用e.preventDefault()來防止anchor tag送出
            e.preventDefault();
        }

    })
})