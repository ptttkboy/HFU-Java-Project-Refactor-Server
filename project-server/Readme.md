# 美食地圖（Android app + Server + 後台管理系統）ver 2.0 *持續更新


## 專案簡介

---
【美食地圖】旨在提供使用者隨時隨地且快速地尋找附近的熱門餐廳，透過手機應用程式，可以尋找已上架餐廳的相關資訊，包含餐廳名稱、圖片、電話、地址，以及地圖等等；
後台管理者則是能夠透過後台管理系統來上架餐廳、管理餐廳，以及管理已註冊的會員。

>此專案為作者本人於2022/6/28至2022/9/13期間，參加職訓局所舉辦的『Java程式暨資料庫開發設計培訓班』之結訓專題『美食地圖 ver 1.0』為基底，以Spring系列框架重構而成。

#### Repos:
- Android app: https://github.com/ptttkboy/HFU-Java-Project-Refactor-Andorid.git
- Spring Boot Server + 後台管理系統: https://github.com/ptttkboy/HFU-Java-Project-Refactor-Server.git 



## 介面與功能
---
#### Android app：
- 瀏覽熱門餐廳資訊
- 註冊及登入會員
- 搜尋餐廳（需註冊會員）

<div style="display: flex; justify-content: space-around">
    <img src="https://i.imgur.com/qaVma6M.png" alt="drawing" width="200"/>
    <img src="https://i.imgur.com/BFHArNp.png" alt="drawing" width="200"/>
    <img src="https://i.imgur.com/4DnTXEV.png" alt="drawing" width="200"/>
</div>
<p style="font-style: italic; text-align: center">Figure.1 Android app介面 - 首頁、搜尋、餐廳資訊</p>

#### 後台管理系統：
- 上架餐廳
- 管理餐廳
- 管理會員

<img src="https://i.imgur.com/TXBVrzW.png" alt="drawing"/>
<p style="font-style: italic; text-align: center">Figure.2 後台管理系統介面 - 餐廳列表</p>
<br>
<img src="https://i.imgur.com/qP6zFYb.png" alt="drawing"/>
<p style="font-style: italic; text-align: center">Figure.3 後台管理系統介面 - 會員管理列表</p>

## 技術與特色

---


![架構圖](https://i.imgur.com/IVqtmKc.png)
<p style="font-style: italic; text-align: center">Figure.4 專案架構圖</p>

### 以Spring框架架構的伺服器
本專案使用Spring系列框架建構伺服器，並透過Spring boot來快速整合相關的dependencies及建立應用程式。
下列為使用到的dependencies一覽：
- Spring Framework
- Spring MVC
- Spring REST
- Spring Data JPA
- Spring Security

### 同時使用RESTful API及MVC架構
本專案伺服器同時採用RESTful API及MVC架構的設計來因應不同前端的需求。Android app能夠向定義好的API URL，以RESTful的風格與伺服器互動；
後台管理系統端則使用MVC架構，透過Spring MVC及視圖模板引擎Thymeleaf來發送、處理請求及響應。

### 驗證與授權機制
本專案的驗證和授權機制皆採用Spring Security來完成。
Android端用使用者於初次登入時使用帳號密碼登入，驗證成功後，伺服器將生成一組有期限的Jwt(Json Web Token)給使用者作為憑證，日後使用者可憑藉該token向伺服器端要求權限資料，不需再重新登入；
而後台管理系統則是皆以帳號密碼登入驗證，並檢核管理者的權限（Authority），以確保只有真正“具有管理員身份”的人能夠操作後台系統，同時啟用CSRF保護來完善跨域安全機制。

### 資料庫的操作與連結
本專案資料庫使用MySQL資料庫，伺服器端以ORM框架Hibernate進行物件與資料表的對映以及封裝資料操作(DML)，並在此之上透過Spring Data JPA來快速便捷地調用。




    
