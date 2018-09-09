package com.change.hippo.admin.controller;

import com.change.hippo.admin.entity.SdnServiceDomianEntity;
import com.change.hippo.admin.entity.SdnServiceEntity;
import com.change.hippo.admin.utils.PageUtils;
import com.change.hippo.admin.utils.Query;
import com.change.hippo.admin.utils.R;
import com.change.hippo.admin.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.change.hippo.utils.result.ActionResult;
import com.change.hippo.admin.service.DictService;
import com.change.hippo.admin.service.DoMainService;
import com.change.hippo.admin.service.InterfaceService;
import com.change.hippo.maintain.model.RouteDefinition;
import com.change.hippo.maintain.service.IServiceEndpoint;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */

@Controller
@RequestMapping("/common/service")
public class ServiceController extends BaseController {
    @Autowired
    private InterfaceService interfaceService;

    @Autowired
    private DictService dictService;

    @Autowired
    DoMainService doMainService;

    @Autowired
    IServiceEndpoint serviceEndpointClient;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping()
    @RequiresPermissions("common:service:service")
    String service() {
        return "common/service/service";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:service:service")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<SdnServiceEntity> dictList = interfaceService.list(query);
        int total = interfaceService.count(query);
        PageUtils pageUtils = new PageUtils(dictList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("common:service:add")
    String add(Model model) {
        // 查询列表数据
        Map<String, List> clazz = new HashMap<>();
        Map<String, Object> map = new HashMap<>(1);
        // 加载服务类型
        map.put("type", "service_type");
        clazz.put("serviceModel", dictService.list(map));
        // 加载请求方式
        map = new HashMap<>(1);
        map.put("type", "request_type");
        clazz.put("request", dictService.list(map));
        // 加载 domain
        clazz.put("domain", doMainService.list(null));

        // 加载 app 权限
        map = new HashMap<>(1);
        map.put("type", "action_type");
        clazz.put("actionType", dictService.list(map));
        model.addAttribute("clazz", clazz);
        return "common/service/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:service:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        // 查询列表数据
        Map<String, List> clazz = new HashMap<>();
        Map<String, Object> map = new HashMap<>(1);
        // 加载服务类型
        map.put("type", "service_type");
        clazz.put("serviceModel", dictService.list(map));
        // 加载请求方式
        map = new HashMap<>(1);
        map.put("type", "request_type");
        clazz.put("request", dictService.list(map));
        // 加载 domain
        clazz.put("domain", doMainService.list(null));

        // 加载 app 权限
        map = new HashMap<>(1);
        map.put("type", "action_type");
        clazz.put("actionType", dictService.list(map));
        model.addAttribute("clazz", clazz);

        SdnServiceEntity sdnService = interfaceService.get(id);
        model.addAttribute("service", sdnService);
        return "common/service/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:service:add")
    public R save(SdnServiceEntity sdnService) {
        sdnService.setCreateTime(new Date());
        sdnService.setCreateBy(getUsername());
        if (sdnService.getStatus()) {
            if (notice(sdnService)) {
                if (interfaceService.save(sdnService) > 0) {
                    return R.ok();
                }
            }
        } else {
            if (interfaceService.save(sdnService) > 0) {
                return R.ok();
            }
        }
        return R.error();
    }


    /**
     * 发布
     */
    @ResponseBody
    @PostMapping("/publish")
    @RequiresPermissions("common:service:publish")
    public R publish(@RequestParam("ids[]") Long[] ids) {
        for (Long l1 : ids) {
            SdnServiceEntity entity = interfaceService.get(l1);
            if (notice(entity)) {
                entity.setStatus(true);
                interfaceService.update(entity);
            }
        }
        return R.ok();
    }

    public Boolean notice(SdnServiceEntity serviceEntity) {
        try {
            RouteDefinition definition = new RouteDefinition();
            BeanUtils.copyProperties(serviceEntity, definition);
            Map<String, Object> map = new HashMap<>(1);
            map.put("name", serviceEntity.getDomain());
            SdnServiceDomianEntity domianEntity = doMainService.list(map).get(0);
            definition.setDomain(domianEntity.getDomain());
            if (StringUtils.isNotBlank(serviceEntity.getMode())) {
                definition.setMethod(serviceEntity.getMode());
            } else {
                definition.setMethod(serviceEntity.getMethod());
            }
            List<RouteDefinition> list = new ArrayList<>();
            list.add(definition);
            ActionResult<Boolean> result = serviceEndpointClient.addService(list);
            return result.getCode().equals("200000");
        } catch (Exception e) {
        }
        return false;
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        int total = interfaceService.count(params);
        return total > 0 ? false : true;
    }

    @GetMapping("/find/{serviceId}")
    @RequiresPermissions("common:service:find")
    String find(@PathVariable("serviceId") String serviceId, Model model) {
        List<String> list = new ArrayList<>();
        list.add(serviceId);
        ActionResult<Collection<RouteDefinition>> result = serviceEndpointClient.find(list);
        result.getCode();
        for (RouteDefinition definition : result.getResult()) {
            model.addAttribute("definition", definition);
        }
        return "common/service/find";
    }


    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("common:service:edit")
    public R update(SdnServiceEntity sdnService) {
        sdnService.setUpdateBy(getUsername());
        sdnService.setUpdateTime(new Date());
        if (sdnService.getStatus()) {
            if (notice(sdnService)) {
                interfaceService.update(sdnService);
                return R.ok();
            }
        } else {
            List<String> list = new ArrayList<>();
            list.add(sdnService.getServiceId());
            if (serviceEndpointClient.delService(list).getCode().equals("200000")) {
                interfaceService.update(sdnService);
                return R.ok();
            }
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:service:remove")
    public R remove(Long id) {
        if (interfaceService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:service:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids) {
        interfaceService.batchRemove(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<SdnServiceEntity> listByType(@PathVariable("serviceId") String serviceId) {
        // 查询列表数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("serviceId", serviceId);
        List<SdnServiceEntity> dictList = interfaceService.list(map);
        return dictList;
    }

}
