package com.change.hippo.admin.controller;

import com.change.hippo.admin.entity.SdnServiceDomianEntity;
import com.change.hippo.admin.utils.PageUtils;
import com.change.hippo.admin.utils.Query;
import com.change.hippo.admin.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.change.hippo.admin.service.DoMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/common/domain")
public class DoMainController extends BaseController {

    @Autowired
    DoMainService doMainService;

    @GetMapping()
    @RequiresPermissions("common:domain:domain")
    String domain() {
        return "common/domain/domain";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:domain:domain")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<SdnServiceDomianEntity> domainList = doMainService.list(query);
        int total = doMainService.count(query);
        PageUtils pageUtils = new PageUtils(domainList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("common:domain:add")
    String add() {
        return "common/domain/add";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:domain:add")
    public R save(SdnServiceDomianEntity domian) {
        domian.setCreateBy(getUsername());
        SdnServiceDomianEntity domianEntity = new SdnServiceDomianEntity(domian);
        if (doMainService.save(domianEntity) > 0) {
            return R.ok();
        }
        return R.error();
    }



    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:domain:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        SdnServiceDomianEntity dict = doMainService.get(id);
        model.addAttribute("domain", dict);
        return "common/domain/edit";
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("common:domain:edit")
    public R update(SdnServiceDomianEntity domian) {
        domian.setUpdateBy(getUsername());
        domian.setUpdateTime(new Date());
        doMainService.update(domian);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:domain:remove")
    public R remove(Long id) {
        if (doMainService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:domain:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        doMainService.batchRemove(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<SdnServiceDomianEntity> listByName(@PathVariable("name") String name) {
        List<SdnServiceDomianEntity> domianList = doMainService.listByName(name);
        return domianList;
    }
}
