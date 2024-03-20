package io.devridge.admin.web;

import io.devridge.admin.dto.course.CourseItemListDto;
import io.devridge.admin.dto.item.BookModifyFormDto;
import io.devridge.admin.dto.item.BookRegisterFormDto;
import io.devridge.admin.dto.item.VideoModifyFormDto;
import io.devridge.admin.dto.item.VideoRegisterFormDto;
import io.devridge.admin.dto.course.CourseDetailDto;
import io.devridge.admin.service.course.AdminCourseDetailService;
import io.devridge.admin.service.course.AdminCourseItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminCourseItemController {

    private final AdminCourseDetailService courseDetailService;
    private final AdminCourseItemService adminCourseItemService;

    @GetMapping("/item")
    public String getCourseDetailListView(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<CourseDetailDto> courseDetailList = courseDetailService.getAllCourseDetail(pageable);
        model.addAttribute("courseDetailList", courseDetailList);

        return "course_item/course_item_main";
    }

    @GetMapping("/item/{courseDetailId}")
    public String courseItemList(@PathVariable Long courseDetailId,
                                 @RequestParam(value = "isActive", defaultValue = "false") boolean isActive,
                                 @RequestParam(value = "beforePage", defaultValue = "0") int beforePage,
                                 Model model) {
        CourseItemListDto courseItemList = adminCourseItemService.findAllItemByCourseDetailId(courseDetailId);

        model.addAttribute("courseItemList", courseItemList);
        model.addAttribute("isActive", isActive);
        model.addAttribute("courseDetailId", courseDetailId);
        model.addAttribute("beforePage", beforePage);

        return "course_item/course_item_list";
    }

   @PostMapping("/item/{courseDetailId}/video")
   public String addCourseVideo(@PathVariable Long courseDetailId,
                                @ModelAttribute VideoRegisterFormDto videoRegisterFormDto) {
       adminCourseItemService.saveVideo(courseDetailId, videoRegisterFormDto);

       return "redirect:/admin/item/" + courseDetailId + "?alertMessage=saved";
   }

   @PutMapping("/item/{courseDetailId}/video")
   public String modifyCourseVideo(@PathVariable Long courseDetailId,
                                @ModelAttribute VideoModifyFormDto videoModifyFormDto) {
       adminCourseItemService.modifyVideo(courseDetailId, videoModifyFormDto);

       return "redirect:/admin/item/" + courseDetailId + "?alertMessage=modified";
   }

   @DeleteMapping("/item/{courseDetailId}/video")
   public String deleteCourseVideo(@PathVariable Long courseDetailId,
                                   @RequestParam Long videoId) {
       adminCourseItemService.deleteVideo(videoId, courseDetailId);
       return "redirect:/admin/item/" + courseDetailId + "?alertMessage=deleted";
   }

   @PostMapping("/item/{courseDetailId}/book")
   public String addCourseBook(@PathVariable Long courseDetailId,
                                @ModelAttribute BookRegisterFormDto bookRegisterFormDto) {

       adminCourseItemService.saveBook(courseDetailId, bookRegisterFormDto);
       return "redirect:/admin/item/" + courseDetailId + "?alertMessage=saved&isActive=true";
   }

   @PutMapping("/item/{courseDetailId}/book")
   public String modifyCourseBook(@PathVariable Long courseDetailId,
                                   @ModelAttribute BookModifyFormDto bookModifyFormDto) {
       adminCourseItemService.modifyBook(courseDetailId, bookModifyFormDto);
       return "redirect:/admin/item/" + courseDetailId + "?alertMessage=modified&isActive=true";
   }

   @DeleteMapping("/item/{courseDetailId}/book")
   public String deleteCourseBook(@PathVariable Long courseDetailId,
                                   @RequestParam Long bookId) {
        adminCourseItemService.deleteBook(bookId, courseDetailId);
        return "redirect:/admin/item/" + courseDetailId + "?alertMessage=deleted&isActive=true";
   }
}
