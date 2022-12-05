package com.hfu.project_server.admin_mvc_controller;

import com.hfu.project_server.entity.Restaurant;
import com.hfu.project_server.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/restaurants")
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    private final RestaurantService restaurantService;

    /**
     * 預處理已提交資料
     *
     * 當html中的input為空時，改成null。
     * @param webDataBinder: input提交過來的資料
     */
    @InitBinder
    public void trimInput(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    /**
     * 餐廳列表&搜尋
     * 在沒有提交搜尋內容時，會顯示所有餐廳列表頁面；當搜尋欄位有輸入值時，會讀取該值，並按名字尋找對應餐廳。
     * @param model: 所有餐廳
     * @return 餐廳列表頁面
     */
    @GetMapping({"/", ""})
    public String showOrFindRestaurants(@RequestParam(value = "type", required = false) String type,
                                        @RequestParam(value = "query", required = false) String query,
                                        Model model) {

        List<Restaurant> restaurants = restaurantService.getRestaurantByQuery(type, query);
        model.addAttribute("restaurants", restaurants);

        return "restaurant-list";
    }

    /**
     * 新增餐廳
     *
     * GET: 取得頁面
     * POST: 提交新增資料
     *
     * @param model: 表單提交之餐廳物件
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "add-restaurant";
    }

    @PostMapping("/add")
    public String processAdd(@Valid @ModelAttribute("restaurant") Restaurant restaurant,
                             BindingResult bindingResult) {
        // 檢驗輸入
        if (bindingResult.hasErrors()) {
            return "add-restaurant";
        }

        restaurantService.addRestaurant(restaurant);
        return "redirect:/admin/restaurants";
    }

    /**
     * 更新餐廳
     *
     * GET: 取得頁面
     * POST: 提交修改資料
     *
     * @param model: 表單提交之餐廳物件
     */
    @GetMapping("/update")
    public String update(@RequestParam("restaurantId") Long id, Model model) {

        Restaurant restaurant = restaurantService.getRestaurantById(id);
        model.addAttribute("restaurant", restaurant);

        return "update-restaurant";
    }

    @PostMapping("/update")
    public String processUpdate(@Valid @ModelAttribute("restaurant") Restaurant restaurant,
                                BindingResult bindingResult){

        // 檢驗輸入
        if (bindingResult.hasErrors()) {
            return "update-restaurant";
        }

        restaurantService.updateRestaurant(restaurant);
        return "redirect:/admin/restaurants";
    }

    /**
     * 刪除餐廳
     */
    @GetMapping("/delete")
    public String delete(@RequestParam("restaurantId") Long id) {

        restaurantService.deleteRestaurantById(id);
        return "redirect:/admin/restaurants";
    }
}
