package com.demo.fightdata;

import org.junit.Test;

/**
 * 判定返修数据查询
 */
public class JigDongTest {


    @Test
    public void getJDResult() {
        String[] list = input.split("\n");
//        System.out.println("总条数：" + list.length);
//        System.out.println("");


        int count = 1;
        for (String s : list) {
            String[] contentArr = s.split("\t");
            if (contentArr.length >= 5 && contentArr[1].contains("京东") && !contentArr[2].contains("-")) {
                System.out.println(count++ + " - " + contentArr[2] + " - " + contentArr[4]);
            }
        }
    }

    String input =
            "利郎电子商务公司\t京东快递\tJDX022860180051-1-1-\t2024-01-13 09:10:02\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24648541070-1-1-\t2024-01-13 09:10:45\twcs\n" +
                    "利郎商城领航\t京东快递\tJDX022876686768\t2024-01-13 09:12:54\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022855930322-1-1-\t2024-01-13 09:12:55\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022804634777\t2024-01-13 09:13:38\twcs\n" +
                    "利郎实体店\t京东快递\tJDVC22625819660-1-1-\t2024-01-13 09:15:42\t王笑\n" +
                    "利郎客户返修\t京东快递\tJDVC22617030606-1-1-\t2024-01-13 09:15:43\t吴云梅\n" +
                    "利郎实体店\t京东快递\tJDVC22622582631-1-1-\t2024-01-13 09:15:43\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22620656578-1-1-\t2024-01-13 09:15:46\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022853097257-1-1-\t2024-01-13 09:15:47\twcs\n" +
                    "利郎实体店\t京东快递\tJDVC22624260719-1-1-\t2024-01-13 09:15:48\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22621745964-1-1-\t2024-01-13 09:15:52\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22621991835-1-1-\t2024-01-13 09:15:55\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22620581563-1-1-\t2024-01-13 09:15:58\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22626033027-1-1-\t2024-01-13 09:16:01\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22622193859-1-1-\t2024-01-13 09:16:04\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22623514052-1-1-\t2024-01-13 09:16:07\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22611279343-1-1-\t2024-01-13 09:16:10\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22620559144-1-1-\t2024-01-13 09:16:12\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22620409420-1-1-\t2024-01-13 09:16:15\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22624226447-1-1-\t2024-01-13 09:16:17\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22621358910-1-1-\t2024-01-13 09:16:20\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22626143661-1-1-\t2024-01-13 09:16:22\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22622342072-1-1-\t2024-01-13 09:16:26\t王笑\n" +
                    "利郎客户返修\t京东快递\tJDVC22621391923-1-1-\t2024-01-13 09:18:36\t吴云梅\n" +
                    "利郎客户返修\t京东快递\tJDVC22623014058-1-1-\t2024-01-13 09:18:39\t吴云梅\n" +
                    "利郎客户返修\t京东快递\tJDVC22621576000-1-1-\t2024-01-13 09:18:43\t吴云梅\n" +
                    "利郎客户返修\t京东快递\tJDVC22621571230-1-1-\t2024-01-13 09:18:46\t吴云梅\n" +
                    "\t京东快递\tJDVA24620822108-1-1-\t2024-01-13 09:19:19\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22618231843-1-1-\t2024-01-13 09:19:47\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22619624591-1-1-\t2024-01-13 09:20:24\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22622663578-1-1-\t2024-01-13 09:21:30\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022873531613-1-1-\t2024-01-13 09:22:10\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24639407231\t2024-01-13 09:22:53\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022823381480\t2024-01-13 09:23:36\twcs\n" +
                    "利郎实体店\t京东快递\tJDVC22622353448-1-1-\t2024-01-13 09:24:15\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22611783505-1-1-\t2024-01-13 09:25:32\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022872281226\t2024-01-13 09:26:26\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24638098576\t2024-01-13 09:26:26\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24649576546\t2024-01-13 09:30:42\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24644843060\t2024-01-13 09:30:43\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022872180398-1-1-\t2024-01-13 09:32:09\twcs\n" +
                    "利郎实体店\t京东快递\tJDVC22609980319-1-1-\t2024-01-13 09:32:24\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022851586211\t2024-01-13 09:32:51\twcs\n" +
                    "利郎实体店\t京东快递\tJDVC22620463026-1-1-\t2024-01-13 09:34:17\t王笑\n" +
                    "\t京东快递\tJDVA24668553640-1-1-\t2024-01-13 09:35:42\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022846638613\t2024-01-13 09:36:25\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022849424284\t2024-01-13 09:36:25\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24655951509-1-1-\t2024-01-13 09:38:33\twcs\n" +
                    "\t京东快递\tJDVA24648678921\t2024-01-13 09:39:16\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022864685188\t2024-01-13 09:39:58\twcs\n" +
                    "\t京东快递\tJDX022852798033-1-1-\t2024-01-13 09:39:59\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022846638613-1-1-\t2024-01-13 09:40:41\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24655951509\t2024-01-13 09:42:07\twcs\n" +
                    "\t京东快递\tJDX022852798033\t2024-01-13 09:42:08\twcs\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDX022872634843\t2024-01-13 09:42:50\twcs\n" +
                    "利郎商城领航\t京东快递\tJDX022903675907-1-1-\t2024-01-13 16:06:34\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022854303491-1-1-\t2024-01-13 16:06:43\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022856426824-1-1-\t2024-01-13 16:06:47\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24640252998-1-1-\t2024-01-13 16:06:50\t王笑\n" +
                    "利郎商城领航\t京东快递\tJDX022876772647-1-1-\t2024-01-13 16:06:57\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24657108279-1-1-\t2024-01-13 16:07:14\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022852259402-1-1-\t2024-01-13 16:07:20\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24670860639-1-1-\t2024-01-13 16:07:25\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022868567390-1-1-\t2024-01-13 16:07:31\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22625906160-1-1-\t2024-01-13 16:08:47\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22618840440-1-1-\t2024-01-13 16:08:54\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22610616844-1-1-\t2024-01-13 16:08:58\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22636513418-1-1-\t2024-01-13 16:09:02\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22626684371-1-1-\t2024-01-13 16:09:05\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22624868252-1-1-\t2024-01-13 16:09:07\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22625370984-1-1-\t2024-01-13 16:09:17\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22625932952-1-1-\t2024-01-13 16:09:21\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDX022869556734-1-1-\t2024-01-13 16:09:54\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24539632286-1-1-\t2024-01-13 16:09:58\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24643458429-1-1-\t2024-01-13 16:10:01\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24637591158-1-1-\t2024-01-13 16:10:04\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24665534304-1-1-\t2024-01-13 16:10:19\t王笑\n" +
                    "利郎市场抖店\t京东快递\tJDX022817988157-1-1-\t2024-01-13 16:10:22\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022845240357-1-1-\t2024-01-13 16:10:25\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022867529027-1-1-\t2024-01-13 16:10:28\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022853075217-1-1-\t2024-01-13 16:10:30\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022858192616-1-1-\t2024-01-13 16:10:37\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22626371901-1-1-\t2024-01-13 16:10:42\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022851292361-1-1-\t2024-01-13 16:11:00\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24640913437-1-1-\t2024-01-13 16:11:04\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDX022858727242-1-1-\t2024-01-13 16:11:06\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022857109160-1-1-\t2024-01-13 16:11:21\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24639509182-1-1-\t2024-01-13 16:11:25\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022852353784-1-1-\t2024-01-13 16:11:29\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022803954647-1-1-\t2024-01-13 16:11:32\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24629039887-1-1-\t2024-01-13 16:11:34\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDX022859993163-1-1-\t2024-01-13 16:11:37\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022866350406-1-1-\t2024-01-13 16:11:38\twcs\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24640341253-1-1-\t2024-01-13 16:11:45\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022851792050-1-1-\t2024-01-13 16:11:47\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022844496334-1-1-\t2024-01-13 16:11:49\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24665836029-1-1-\t2024-01-13 16:11:56\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022875395719-1-1-\t2024-01-13 16:11:59\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022821734211-1-1-\t2024-01-13 16:12:05\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022809466436-1-1-\t2024-01-13 16:12:08\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022879250968-1-1-\t2024-01-13 16:12:12\t王笑\n" +
                    "\t京东快递\tJDX022836941653-1-1-\t2024-01-13 16:12:14\t吴云梅\n" +
                    "利郎领航抖店\t京东快递\tJDX022845854929-1-1-\t2024-01-13 16:12:15\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022838394954-1-1-\t2024-01-13 16:12:21\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022821852210-1-1-\t2024-01-13 16:12:23\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022860039694-1-1-\t2024-01-13 16:12:25\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022858641032-1-1-\t2024-01-13 16:12:28\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24625113855-1-1-\t2024-01-13 16:12:29\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22621413190-1-1-\t2024-01-13 16:12:31\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022816756282-1-1-\t2024-01-13 16:12:33\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24641371563-1-1-\t2024-01-13 16:12:45\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022853975314-1-1-\t2024-01-13 16:12:48\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022819348526-1-1-\t2024-01-13 16:12:51\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022816992999-1-1-\t2024-01-13 16:12:54\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24662618285-1-1-\t2024-01-13 16:12:58\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24616629940-1-1-\t2024-01-13 16:13:02\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24659638950-1-1-\t2024-01-13 16:13:03\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022804903571-1-1-\t2024-01-13 16:13:05\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022858684016-1-1-\t2024-01-13 16:13:07\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022767237292-1-1-\t2024-01-13 16:13:11\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24490803300-1-1-\t2024-01-13 16:13:24\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24580973812-1-1-\t2024-01-13 16:13:28\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022800740271-1-1-\t2024-01-13 16:13:31\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24558694825-1-1-\t2024-01-13 16:13:35\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22618597511-1-1-\t2024-01-13 16:13:42\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022846896624-1-1-\t2024-01-13 16:13:49\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24646170543-1-1-\t2024-01-13 16:13:53\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24661611259-1-1-\t2024-01-13 16:14:05\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022826187778-1-1-\t2024-01-13 16:14:15\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022857836610-1-1-\t2024-01-13 16:14:18\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24662728436-1-1-\t2024-01-13 16:14:20\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24428079391\t2024-01-13 16:14:28\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022869813326-1-1-\t2024-01-13 16:14:28\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022857281501-1-1-\t2024-01-13 16:14:30\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022866460223-1-1-\t2024-01-13 16:14:33\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24658668067-1-1-\t2024-01-13 16:14:58\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022852330935\t2024-01-13 16:15:01\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022861860422-1-1-\t2024-01-13 16:15:03\t王笑\n" +
                    "\t京东快递\tJDX022873943839-1-1-\t2024-01-13 16:15:10\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022860950235-1-1-\t2024-01-13 16:15:10\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24623615617-1-1-\t2024-01-13 16:15:34\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24625698888-1-1-\t2024-01-13 16:15:36\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022851551130-1-1-\t2024-01-13 16:15:39\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022850776607-1-1-\t2024-01-13 16:15:53\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDX022840396160-1-1-\t2024-01-13 16:15:58\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24637505720-1-1-\t2024-01-13 16:16:00\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022829219923-1-1-\t2024-01-13 16:16:03\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022851924251-1-1-\t2024-01-13 16:16:06\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022840288412-1-1-\t2024-01-13 16:16:10\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24614360700-1-1-\t2024-01-13 16:16:13\t王笑\n" +
                    "\t京东快递\tJDX022829113967-1-1-\t2024-01-13 16:16:15\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022835247710-1-1-\t2024-01-13 16:16:19\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24633422861-1-1-\t2024-01-13 16:16:22\t王笑\n" +
                    "\t京东快递\tJDX022831519792-1-1-\t2024-01-13 16:16:27\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022814591360-1-1-\t2024-01-13 16:16:30\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022875840958-1-1-\t2024-01-13 16:16:35\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022871534432-1-1-\t2024-01-13 16:16:35\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24633476977-1-1-\t2024-01-13 16:16:40\t王笑\n" +
                    "利郎市场抖店\t京东快递\tJDX022851721193-1-1-\t2024-01-13 16:16:43\t王笑\n" +
                    "利郎市场抖店\t京东快递\tJDX022778476545-1-1-\t2024-01-13 16:16:48\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022841050596-1-1-\t2024-01-13 16:16:53\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022812299580-1-1-\t2024-01-13 16:16:58\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22620018030-1-1-\t2024-01-13 16:17:03\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022866923210-1-1-\t2024-01-13 16:17:13\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24641173985-1-1-\t2024-01-13 16:17:15\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022837035516-1-1-\t2024-01-13 16:17:19\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24640449028-1-1-\t2024-01-13 16:17:21\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022857048283-1-1-\t2024-01-13 16:17:25\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022851538826-1-1-\t2024-01-13 16:17:30\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022874614376-1-1-\t2024-01-13 16:17:36\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022839626318-1-1-\t2024-01-13 16:17:39\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022823381329-1-1-\t2024-01-13 16:17:40\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24642720834-1-1-\t2024-01-13 16:17:55\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24641167811-1-1-\t2024-01-13 16:17:57\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24654810534-1-1-\t2024-01-13 16:18:00\twcs\n" +
                    "利郎实体店\t京东快递\tJDVC22621806113\t2024-01-13 16:18:17\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022857997458-1-1-\t2024-01-13 16:18:34\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022772795960-1-1-\t2024-01-13 16:18:36\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022848633975-1-1-\t2024-01-13 16:18:38\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24674987137-1-1-\t2024-01-13 16:18:42\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022856552447-1-1-\t2024-01-13 16:18:49\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022832622887-1-1-\t2024-01-13 16:18:54\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDX022858190572-1-1-\t2024-01-13 16:18:59\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24640419435-1-1-\t2024-01-13 16:19:02\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24648344060-1-1-\t2024-01-13 16:19:05\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022850828546-1-1-\t2024-01-13 16:19:07\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022809983934-1-1-\t2024-01-13 16:19:10\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022822679300-1-1-\t2024-01-13 16:19:13\t王笑\n" +
                    "利郎市场抖店\t京东快递\tJDX022807858575-1-1-\t2024-01-13 16:19:17\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022826122880-1-1-\t2024-01-13 16:19:20\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24662716582-1-1-\t2024-01-13 16:19:25\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022879131332-1-1-\t2024-01-13 16:19:28\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022842568678-1-1-\t2024-01-13 16:19:30\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022829203185-1-1-\t2024-01-13 16:19:32\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022798539093-1-1-\t2024-01-13 16:19:38\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24643421422-1-1-\t2024-01-13 16:19:47\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022837685996-1-1-\t2024-01-13 16:19:52\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24658460851-1-2-\t2024-01-13 16:20:01\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022842539115-1-1-\t2024-01-13 16:20:02\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022810434469-1-1-\t2024-01-13 16:20:07\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24637083000-1-1-\t2024-01-13 16:20:08\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022822694670-1-1-\t2024-01-13 16:20:24\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022667053766-1-1-\t2024-01-13 16:20:26\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022822068083-1-1-\t2024-01-13 16:20:29\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022843180922-1-1-\t2024-01-13 16:20:50\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24643034827-1-1-\t2024-01-13 16:20:53\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24640707615-1-1-\t2024-01-13 16:20:56\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022853182963-1-1-\t2024-01-13 16:20:58\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24630709138-1-1-\t2024-01-13 16:21:02\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022824844610-1-1-\t2024-01-13 16:21:03\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022823239757-1-1-\t2024-01-13 16:21:06\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022813049104-1-1-\t2024-01-13 16:21:11\t王笑\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDX022858012877-1-1-\t2024-01-13 16:21:13\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022851822030-1-1-\t2024-01-13 16:21:17\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24636712327-1-1-\t2024-01-13 16:21:20\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22619683039-1-1-\t2024-01-13 16:21:23\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24623584565-1-1-\t2024-01-13 16:21:28\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24658460851-2-2-\t2024-01-13 16:21:30\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022866290850-1-1-\t2024-01-13 16:21:32\t王笑\n" +
                    "利郎实体店\t京东快递\tJDVC22602048673-1-1-\t2024-01-13 16:21:38\t王笑\n" +
                    "利郎电子商务公司\t京东快递\tJDX022859708134-1-1-\t2024-01-13 16:22:03\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022791176392-1-1-\t2024-01-13 16:22:09\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022859684966-1-1-\t2024-01-13 16:22:13\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022870430475-1-1-\t2024-01-13 16:22:14\twcs\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24660210500\t2024-01-13 16:22:15\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022848581578-1-1-\t2024-01-13 16:22:15\twcs\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24637161229-1-1-\t2024-01-13 16:22:57\twcs\n" +
                    "利郎市场抖店\t京东快递\tJDX022846528939-1-1-\t2024-01-13 16:22:57\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022860950235\t2024-01-13 16:23:39\twcs\n" +
                    "利郎市场抖店\t京东快递\tJDX022837809388-1-1-\t2024-01-13 16:23:40\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022844642378-1-1-\t2024-01-13 16:23:40\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022807906483-1-1-\t2024-01-13 16:23:40\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24643430000-1-1-\t2024-01-13 16:24:22\twcs\n" +
                    "利郎电子商务公司\t京东快递\tJDVA24640750159-1-1-\t2024-01-13 16:24:22\twcs\n" +
                    "福建简尚公司（新零售）\t京东快递\tJDVA24660210500-1-1-\t2024-01-13 16:24:23\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022870101679-1-1-\t2024-01-13 16:24:23\twcs\n" +
                    "利郎领航抖店\t京东快递\tJDX022832369510-1-1-\t2024-01-13 16:27:42\t王笑\n" +
                    "利郎领航抖店\t京东快递\tJDX022862612040-1-1-\t2024-01-13 16:27:45\t王笑\n" +
                    "利郎电子商务公司\t其他\t73515734745728\t2024-01-13 08:14:54\twcs\n";
}