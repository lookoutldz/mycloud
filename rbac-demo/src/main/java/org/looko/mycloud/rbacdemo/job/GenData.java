package org.looko.mycloud.rbacdemo.job;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.rbacdemo.domain.*;
import org.looko.mycloud.rbacdemo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class GenData {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private TradeorderService tradeorderService;

    public void genAll() {
        List<String> usernames;
        List<String> contractNames;
        try {
            usernames = getUsername();
            contractNames = getContractName();
        } catch (IOException e) {
            log.error("usernames/ContractNames 读取出错");
            return;
        }

        Department lawDept = new Department(null,"法务部", 1, null);
        Department expensiveIronDept = new Department(null,"贵金属部", 1, null);
        Department colorIronDept = new Department(null,"有色金属部", 1, null);
        Set<Department> departments = Set.of(lawDept, expensiveIronDept, colorIronDept);
        departmentService.saveBatch(departments);


        Role admin = new Role(null, "admin", 0L);
        Role ceo = new Role(null, "CEO", 0L);
        Role lawMinister = new Role(null, "法务部长", lawDept.getId());
        Role expensiveIronMinister = new Role(null, "贵金属部部长", expensiveIronDept.getId());
        Role colorIronMinister = new Role(null, "有色金属部部长", colorIronDept.getId());
        Role expensiveIronResearcher = new Role(null, "贵金属部研究员", expensiveIronDept.getId());
        Role colorIronResearcher = new Role(null, "有色金属部研究员", expensiveIronDept.getId());
        List<Role> roles = List.of(admin, ceo, lawMinister, expensiveIronMinister, expensiveIronResearcher, colorIronMinister, colorIronResearcher);
        roleService.saveBatch(roles);

        List<User> users = new ArrayList<>();
        users.add(new User(null, "administrator", "123456", null));
        for (String username : usernames) {
            users.add(new User(null, username, "123456", null));
        }
        userService.saveBatch(users);

        List<RoleUser> roleUsers = new ArrayList<>();
        roleUsers.add(new RoleUser(ceo.getId(), users.get(0).getId()));
        roleUsers.add(new RoleUser(lawMinister.getId(), users.get(1).getId()));
        roleUsers.add(new RoleUser(expensiveIronMinister.getId(), users.get(2).getId()));
        roleUsers.add(new RoleUser(colorIronMinister.getId(), users.get(3).getId()));
        for (int i = 4; i < users.size(); i++) {
            Role randRole = System.currentTimeMillis() % 2 == 0 ? expensiveIronResearcher : colorIronResearcher;
            roleUsers.add(new RoleUser(randRole.getId(), users.get(i).getId()));
        }
        roleUserService.saveBatch(roleUsers);


        List<Contract> contracts = new ArrayList<>();
        for (String contractName : contractNames) {
            Department randDepartment = System.currentTimeMillis() % 2 == 0 ? expensiveIronDept : colorIronDept;
            contracts.add(new Contract(null, contractName, randDepartment.getId()));
        }
        contractService.saveBatch(contracts);

        List<Tradeorder> tradeorders = new ArrayList<>();
        for (Contract contract : contracts) {
            tradeorders.add(new Tradeorder(null, contract.getName() + "订单1", contract.getId(), contract.getDepartmentId()));
            tradeorders.add(new Tradeorder(null, contract.getName() + "订单2", contract.getId(), contract.getDepartmentId()));
        }
        tradeorderService.saveBatch(tradeorders);
    }

    @Autowired
    private ResourceLoader resourceLoader;
    private List<String> getFileLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        Resource resource = resourceLoader.getResource(path);
        if (resource.exists()) {
            InputStream inputStream = resource.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.trim());
                }
            }
        } else {
            log.error("文件不存在");
        }
        return lines;
    }

    public List<String> getUsername() throws IOException {
        return getFileLines("classpath:data/username.txt");
    }

    public List<String> getContractName() throws IOException {
        return getFileLines("classpath:data/contract_name.txt");
    }

    public void genPermission() {

    }
}
