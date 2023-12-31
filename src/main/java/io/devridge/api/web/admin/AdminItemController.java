package io.devridge.api.web.admin;

import io.devridge.api.dto.admin.CourseDetailDto;
import io.devridge.api.dto.admin.item.*;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.NotFoundCourseBookException;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import io.devridge.api.service.admin.AdminCourseDetailService;
import io.devridge.api.service.admin.AdminCourseItemService;
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
public class AdminItemController {
    private final AdminCourseDetailService adminCourseDetailService;
    private final AdminCourseItemService adminCourseItemService;

    @GetMapping("/item")
    public String courseDetailListViewWithItem(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<CourseDetailDto> courseDetailList = adminCourseDetailService.getAllCourseDetail(pageable);
        model.addAttribute("courseDetailList", courseDetailList);

        return "course_item/course_item_main";
    }

    @GetMapping("/item/{courseDetailId}")
    public String courseItemList(@PathVariable Long courseDetailId,
                                 @RequestParam(value = "isActive", defaultValue = "false") boolean isActive,
                                 @RequestParam(value = "beforePage", defaultValue = "0") int beforePage,
                                 Model model) {
        try {
            CourseItemListDto courseItemList = adminCourseItemService.findAllItemByCourseDetailId(courseDetailId);

            model.addAttribute("courseItemList", courseItemList);
            model.addAttribute("isActive", isActive);
            model.addAttribute("courseDetailId", courseDetailId);
            model.addAttribute("beforePage", beforePage);

            return "course_item/course_item_list";
        } catch (CourseDetailNotFoundException e) {
            log.error("코스 상세를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item?error=not_found_course_detail";
        }
    }

    @PostMapping("/item/{courseDetailId}/video")
    public String addCourseVideo(@PathVariable Long courseDetailId,
                                 @ModelAttribute VideoRegisterFormDto videoRegisterFormDto) {
        try {
            adminCourseItemService.saveVideo(courseDetailId, videoRegisterFormDto);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=saved";
        } catch (CourseDetailNotFoundException e) {
            log.error("코스 상세를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item?error=not_found_course_detail";
        }
    }

    @PutMapping("/item/{courseDetailId}/video")
    public String modifyCourseVideo(@PathVariable Long courseDetailId,
                                 @ModelAttribute VideoModifyFormDto videoModifyFormDto) {
        try {
            adminCourseItemService.modifyVideo(courseDetailId, videoModifyFormDto);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=modified";
        } catch (CourseDetailNotFoundException e ) {
            log.error("코스 상세를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item?error=not_found_course_detail";
        } catch (NotFoundCourseVideoException e) {
            log.error("코스 비디오를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item/" + courseDetailId + "?error=fail_modified";
        }
    }

    @DeleteMapping("/item/{courseDetailId}/video")
    public String deleteCourseVideo(@PathVariable Long courseDetailId,
                                    @RequestParam Long videoId) {
        try {
            adminCourseItemService.deleteVideo(videoId);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=deleted";
        } catch (NotFoundCourseVideoException e) {
            log.error("코스 비디오를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item/" + courseDetailId + "?error=fail_deleted";
        }
    }

    @PostMapping("/item/{courseDetailId}/book")
    public String addCourseBook(@PathVariable Long courseDetailId,
                                 @ModelAttribute BookRegisterFormDto bookRegisterFormDto) {
        try {
            adminCourseItemService.saveBook(courseDetailId, bookRegisterFormDto);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=saved&isActive=true";
        } catch (CourseDetailNotFoundException e) {
            log.error("코스 상세를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item?error=not_found_course_detail";
        }
    }

    @PutMapping("/item/{courseDetailId}/book")
    public String modifyCourseBook(@PathVariable Long courseDetailId,
                                    @ModelAttribute BookModifyFormDto bookModifyFormDto) {
        try {
            adminCourseItemService.modifyBook(courseDetailId, bookModifyFormDto);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=modified&isActive=true";
        } catch (CourseDetailNotFoundException e ) {
            log.error("코스 상세를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item?error=not_found_course_detail";
        } catch (NotFoundCourseBookException e) {
            log.error("코스 책을 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item/" + courseDetailId + "?error=fail_modified&isActive=true";
        }
    }

    @DeleteMapping("/item/{courseDetailId}/book")
    public String deleteCourseBook(@PathVariable Long courseDetailId,
                                    @RequestParam Long bookId) {
        try {
            adminCourseItemService.deleteBook(bookId);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=deleted&isActive=true";
        } catch (NotFoundCourseBookException e) {
            log.error("코스 비디오를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item/" + courseDetailId + "?error=fail_deleted&isActive=true";
        }
    }
}
