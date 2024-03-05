import MonthBasicDataBean from '../../bean/MonthBasicDataBean';

export default class Data_PG_10_2024 {

  /**
   * 获取基础数据
   */
  getBasicList(): Array<MonthBasicDataBean> {
    let basicList: Array<MonthBasicDataBean> = []
    // basicList.push(new MonthBasicDataBean('24', '12', Data_PG_10_2024.month_12))
    // basicList.push(new MonthBasicDataBean('24', '11', Data_PG_10_2024.month_11))
    // basicList.push(new MonthBasicDataBean('24', '10', Data_PG_10_2024.month_10))
    // basicList.push(new MonthBasicDataBean('24', '9', Data_PG_10_2024.month_9))
    // basicList.push(new MonthBasicDataBean('24', '8', Data_PG_10_2024.month_8))
    // basicList.push(new MonthBasicDataBean('24', '7', Data_PG_10_2024.month_7))
    // basicList.push(new MonthBasicDataBean('24', '6', Data_PG_10_2024.month_6))
    // basicList.push(new MonthBasicDataBean('24', '5', Data_PG_10_2024.month_5))
    // basicList.push(new MonthBasicDataBean('24', '4', Data_PG_10_2024.month_4))
    // basicList.push(new MonthBasicDataBean('24', '3', Data_PG_10_2024.month_3))
    basicList.push(new MonthBasicDataBean('24', '2', Data_PG_10_2024.month_2))
    basicList.push(new MonthBasicDataBean('24', '1', Data_PG_10_2024.month_1))

    return basicList;
  }


  // static month_12: string =
  // static month_11: string =
  // static month_10: string =
  // static month_9: string =
  // static month_8: string =
  // static month_7: string =
  // static month_6: string =
  // static month_5: string =
  // static month_4: string =
  // static month_3: string =
  static month_2: string =
  "*\t31\t15.00\t-1\t4123\t4146\n"+
  "-1\t1\t13.45\t1\t4146\t4136\n"+
  "\t1\t14.15\t-1\t4121\t4174\n"+
  "\t1\t21.20\t1\t4174\t4147\n"+
  "\t2\t11.05\t-1\t4147\t4122\n"+
  "\t5\t14.05\t1\t4122\t4227\n"+
  "/\t6\t21.30\t-1\t4227\t4164\n"+
  "\t7\t10.45\t-1\t4150\t4179\n"+
  "\t8\t13.45\t1\t4179\t4158\n"+
  "/\t19\t9.50\t-1\t4186\t4154\n"+
  "\t19\t10.55\t-1\t4136\t4227\n"+
  "/\t19\t21.30\t-1\t4211\t4232\n"+
  "*\t20\t9.20\t1\t4232\t4227\n"+
  "\t20\t21.10\t-1\t4162\t4218\n"+
  "\t21\t10.45\t1\t4237\t4194\n"+
  "\t21\t23.00\t-1\t4192\t4213\n"+
  "\t22\t11.15\t1\t4213\t4204\n"+
  "\t23\t22.30\t1\t4222\t4195\n"+
  "*\t23\t10.45\t-1\t4191\t4076\n"+
  "/\t26\t10.10\t1\t4076\t4138\n"+
  "\t26\t14.55\t1\t4138\t4146\n"+
  "\t27\t14.15\t1\t4175\t4172\n"+
  "\t28\t15.00\t-1\t4693\t4726\n"+
  "\t28\t22.50\t1\t4726\t4690\n"+
  "\t29\t9.30\t-1\t4690\t4676"

  static month_1: string =
  "*\t2\t9.10\t1\t4922\t4976\n"+
  "/\t2\t9.30\t-1\t4976\t5010\n"+
  "*\t2\t10.10\t1\t5010\t4940\n"+
  "*\t2\t22.1\t-1\t4940\t4689\n"+
  "/\t4\t22.3\t1\t4689\t4659\n"+
  "*\t4\t22.5\t-1\t4659\t4360\n"+
  "/\t10\t21.3\t1\t4360\t4382\n"+
  "\t10\t22.1\t-1\t4382\t4383\n"+
  "\t11\t13.45\t1\t4383\t4356\n"+
  "\t12\t13.45\t-1\t4356\t4350\n"+
  "\t15\t13.55\t1\t4350\t4344\n"+
  "\t16\t10.35\t1\t4358\t4372\n"+
  "\t17\t10.45\t-1\t4368\t4350\n"+
  "\t18\t14.45\t1\t4356\t4346\n"+
  "\t19\t22.5\t-1\t4235\t4270\n"+
  "\t22\t10.35\t1\t4273\t4208\n"+
  "\t22\t14.25\t-1\t4208\t4278\n"+
  "\t22\t21.4\t1\t4292\t4265\n"+
  "\t23\t14.55\t1\t4291\t4262\n"+
  "\t23\t22.1\t-1\t4252\t4272\n"+
  "-1\t24\t13.55\t1\t4272\t4286\n"+
  "*\t24\t14.35\t1\t4286\t4330\n"+
  "/\t24\t21.4\t-1\t4330\t4332\n"+
  "*\t25\t10.55\t1\t4332\t4345\n"+
  "-1\t26\t10\t-1\t4345\t4359\n"+
  "-1\t26\t13.45\t1\t4359\t4340\n"+
  "\t26\t14.25\t-1\t4340\t4280\n"+
  "\t29\t14.15\t1\t4275\t4350\n"+
  "/\t30\t14.55\t1\t4342\t4284\n"+
  "*\t30\t21.10\t-1\t4284\t4240\n"+
  "/\t30\t22.10\t1\t4240\t4249\n"+
  "*\t31\t9.20\t-1\t4249\t4128\n"+
  "/\t31\t14.45\t1\t4128\t4123"
}
