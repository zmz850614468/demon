import MonthBasicDataBean from '../bean/MonthBasicDataBean'

export default class Data_RM_10_2023 {
  /**
   * 获取基础数据
   */
  getBasicList(): Array<MonthBasicDataBean> {
    let basicList: Array<MonthBasicDataBean> = []
    basicList.push(new MonthBasicDataBean('23', '11', Data_RM_10_2023.month_11))
    basicList.push(new MonthBasicDataBean('23', '10', Data_RM_10_2023.month_10))
    basicList.push(new MonthBasicDataBean('23', '9', Data_RM_10_2023.month_9))
    basicList.push(new MonthBasicDataBean('23', '8', Data_RM_10_2023.month_8))
    // basicList.push(new MonthBasicDataBean('23', '7', Data_RM_10_2023.month_7))
    // basicList.push(new MonthBasicDataBean('23', '6', Data_RM_10_2023.month_6))
    // basicList.push(new MonthBasicDataBean('23', '5', Data_RM_10_2023.month_5))
    // basicList.push(new MonthBasicDataBean('23', '4', Data_RM_10_2023.month_4))

    return basicList;
  }

  static month_11: string =
    "\t30\t21.10\t\t-1\t2929\t2875\n" +
    "*\t1\t21.10\t\t1\t2875\t2930\n" +
    "/\t2\t14.05\t\t-1\t2930\t2939\n" +
    "*\t2\t21.10\t\t1\t2939\t2996\n" +
    "\t7\t9.20\t\t1\t3026\t3074\n" +
    "*\t8\t14.45\t\t-1\t3070\t3025\n" +
    "/\t9\t21.50\t\t1\t3025\t3027\n" +
    "*\t9\t22.40\t\t-1\t3027\t2951\n" +
    "\t13\t22.50\t\t1\t2954\t2975\n" +
    "\t15\t9.20\t\t1\t3011\t2986\n" +
    "\t15\t21.30\t\t-1\t2986\t2999\n" +
    "\t16\t11.15\t\t-1\t2977\t2904\n" +
    "\t20\t21.20\t\t-1\t2885\t2908\n" +
    "*\t21\t9.40\t\t1\t2908\t2918\n" +
    "/\t21\t21.30\t\t-1\t2918\t2927\n" +
    "*\t21\t22.40\t\t1\t2927\t2889\n" +
    "*\t22\t13.35\t\t-1\t2888\t2853\n" +
    "/\t22\t14.25\t\t1\t2853\t2840\n" +
    "*\t22\t21.10\t\t-1\t2840\t2872\n" +
    "*\t24\t9.10\t\t1\t2874\t2914\n" +
    "/\t24\t21.50\t\t-1\t2914\t2916\n" +
    "*\t24\t22.50\t\t1\t2916\t2866\n" +
    "*\t27\t9.40\t\t-1\t2866\t2800\n" +
    "/\t27\t21.50\t\t1\t2800\t2780\n" +
    "*\t27\t22.40\t\t-1\t2780\t2837\n" +
    "\t28\t10.35\t\t1\t2837\t2834\n" +
    "\t29\t22.30\t\t1\t2851\t2831"
  static month_10: string =
    "\t9\t11.15\t\t-1\t3007\t2891\n" +
    "\t12\t22.00\t\t-1\t2870\t2900\n" +
    "\t13\t9.30\t\t1\t2900\t2887\n" +
    "\t13\t22.10\t0\t-1\t2887\t2878\n" +
    "\t13\t22.40\t1\t-1\t2878\t2908\n" +
    "\t16\t10.35\t\t1\t2928\t2938\n" +
    "\t17\t9.30\t\t-1\t2926\t2936\n" +
    "\t17\t22.30\t\t1\t2942\t2926\n" +
    "\t18\t14.05\t\t-1\t2908\t2924\n" +
    "*\t19\t10.45\t\t1\t2924\t2946\n" +
    "/\t19\t21.30\t\t-1\t2946\t2918\n" +
    "\t20\t13.45\t\t-1\t2916\t2932\n" +
    "\t20\t21.50\t\t1\t2948\t2914\n" +
    "\t23\t11.15\t\t1\t2970\t2935\n" +
    "\t23\t21.10\t\t-1\t2917\t2929\n" +
    "*\t24\t14.45\t\t1\t2924\t3018\n" +
    "/\t25\t9.30\t\t-1\t3018\t2945\n" +
    "\t25\t13.55\t\t-1\t2943\t2934\n" +
    "\t26\t11.05\t\t1\t2934\t2931\n" +
    "\t27\t21.40\t\t1\t2964\t2980"
  static month_9: string =
    "\t1\t10.55\t\t-1\t3374\t3398\n" +
    "\t1\t21.10\t\t1\t3420\t3368\n" +
    "\t1\t23.00\t\t-1\t3374\t3293\n" +
    "/\t5\t9.50\t\t-1\t3321\t3313\n" +
    "\t5\t14.05\t\t1\t3313\t3290\n" +
    "\t5\t14.55\t\t-1\t3290\t3326\n" +
    "\t6\t9.10\t\t1\t3336\t3361\n" +
    "\t7\t9.40\t\t-1\t3361\t3385\n" +
    "*\t7\t21.40\t\t-1\t3354\t3253\n" +
    "/\t8\t10.10\t\t1\t3253\t3234\n" +
    "*\t8\t15.00\t\t-1\t3234\t3258\n" +
    "\t8\t23.00\t\t1\t3258\t3261\n" +
    "*\t12\t10.00\t\t-1\t3261\t3087\n" +
    "/\t13\t22.30\t\t1\t3087\t3103\n" +
    "*\t14\t10.10\t\t-1\t3103\t3120\n" +
    "\t14\t22.30\t\t1\t3114\t3153\n" +
    "\t18\t9.10\t\t-1\t3153\t3139\n" +
    "\t19\t14.55\t\t1\t3139\t3123\n" +
    "\t20\t13.35\t\t1\t3178\t3154\n" +
    "\t21\t10.00\t\t-1\t3154\t3119\n" +
    "\t22\t21.10\t\t1\t3151\t3128\n" +
    "\t25\t14.35\t\t-1\t3121\t3152\n" +
    "\t26\t11.15\t\t1\t3173\t3112\n" +
    "\t26\t14.55\t\t-1\t3112\t3142\n" +
    "\t27\t10.00\t0\t1\t3142\t3156\n" +
    "\t27\t13.55\t1\t1\t3156\t3132\n" +
    "\t28\t10.10\t\t-1\t3118\t3100"
  static month_8: string =
    "\t31\t21.10\t\t1\t3649\t3649\n" +
    "\t2\t9.20\t\t1\t3682\t3640\n" +
    "\t2\t21.40\t\t-1\t3640\t3680\n" +
    "\t3\t13.35\t\t1\t3671\t3734\n" +
    "\t4\t21.50\t\t-1\t3727\t3758\n" +
    "\t7\t9.40\t\t1\t3758\t3728\n" +
    "\t7\t14.15\t\t1\t3759\t3699\n" +
    "\t7\t21.40\t\t-1\t3699\t3730\n" +
    "*\t8\t13.55\t\t1\t3734\t3834\n" +
    "/\t9\t9.30\t\t-1\t3834\t3744\n" +
    "*\t10\t14.25\t\t1\t3789\t3882\n" +
    "/\t10\t21.40\t\t-1\t3882\t3794\n" +
    "\t11\t10.10\t0\t-1\t3795\t3855\n" +
    "\t11\t21.40\t\t-1\t3810\t3837\n" +
    "\t14\t9.10\t\t1\t3904\t3936\n" +
    "\t16\t11.15\t\t1\t3953\t4219\n" +
    "/\t21\t22.20\t\t1\t3433\t3427\n" +
    "\t21\t22.30\t\t-1\t3427\t3356\n" +
    "\t23\t9.50\t\t1\t3356\t3325\n" +
    "\t23\t14.35\t\t-1\t3318\t3328\n" +
    "\t24\t14.35\t\t-1\t3320\t3349\n" +
    "\t24\t22.50\t\t1\t3358\t3365\n" +
    "\t25\t21.20\t\t-1\t3365\t3422\n" +
    "\t28\t10.45\t\t1\t3454\t3389\n" +
    "\t28\t21.20\t\t1\t3440\t3389\n" +
    "\t29\t9.30\t\t-1\t3389\t3424\n" +
    "\t29\t14.25\t\t1\t3428\t3382\n" +
    "\t30\t9.10\t\t-1\t3369\t3384\n" +
    "\t30\t21.1\t\t1\t3402\t3392\n" +
    "\t31\t21.40\t\t-1\t3378\t3420"
}