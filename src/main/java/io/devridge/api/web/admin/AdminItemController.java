package io.devridge.api.web.admin;

import io.devridge.api.dto.admin.CourseDetailDto;
import io.devridge.api.dto.admin.item.CourseItemListDto;
import io.devridge.api.dto.admin.item.VideoModifyFormDto;
import io.devridge.api.dto.admin.item.VideoRegisterFormDto;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import io.devridge.api.service.admin.CourseDetailService;
import io.devridge.api.service.admin.CourseItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminItemController {
    private final CourseDetailService courseDetailService;
    private final CourseItemService courseItemService;

    @GetMapping("/item")
    public String courseDetailListViewWithItem(Model model) {
        List<CourseDetailDto> courseDetailList = courseDetailService.getAllCourseDetail();
        model.addAttribute("courseDetailList", courseDetailList);

        return "course_item/course_item_main";
    }

    @GetMapping("/item/{courseDetailId}")
    public String courseItemList(@PathVariable Long courseDetailId,
                                 @RequestParam(value = "isActive", defaultValue = "false") boolean isActive,
                                 Model model) {
        try {
            CourseItemListDto courseItemList = courseItemService.findAllItemByCourseDetailId(courseDetailId);

            model.addAttribute("courseItemList", courseItemList);
            model.addAttribute("isActive", isActive);
            model.addAttribute("courseDetailId", courseDetailId);

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
            courseItemService.saveVideo(courseDetailId, videoRegisterFormDto);
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
            courseItemService.modifyVideo(courseDetailId, videoModifyFormDto);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=modified";
        } catch (CourseDetailNotFoundException e ) {
            log.error("코스 상세를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item?error=not_found_course_detail";
        } catch (NotFoundCourseVideoException e) {
            log.error("코스 비디오를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item/" + courseDetailId + "?error=fail_modify_course_video";
        }
    }

    @DeleteMapping("/item/{courseDetailId}/video")
    public String deleteCourseVideo(@PathVariable Long courseDetailId,
                                    @RequestParam Long videoId) {
        try {
            courseItemService.deleteVideo(videoId);
            return "redirect:/admin/item/" + courseDetailId + "?alertMessage=deleted";
        } catch (NotFoundCourseVideoException e) {
            log.error("코스 비디오를 찾을 수 없습니다. = {0}", e);
            return "redirect:/admin/item/" + courseDetailId + "?error=fail_delete_course_video";
        }
    }
}
