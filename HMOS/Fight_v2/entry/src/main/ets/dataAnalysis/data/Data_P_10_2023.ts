import MonthBasicDataBean from '../bean/MonthBasicDataBean'

export default class Data_P_10_2023 {

  /**
   * 获取基础数据
   */
  getBasicList(): Array<MonthBasicDataBean> {
    let basicList: Array<MonthBasicDataBean> = []
    basicList.push(new MonthBasicDataBean('23', '11', Data_P_10_2023.month_11))
    basicList.push(new MonthBasicDataBean('23', '10', Data_P_10_2023.month_10))
    basicList.push(new MonthBasicDataBean('23', '9', Data_P_10_2023.month_9))
    basicList.push(new MonthBasicDataBean('23', '8', Data_P_10_2023.month_8))
    basicList.push(new MonthBasicDataBean('23', '7', Data_P_10_2023.month_7))
    basicList.push(new MonthBasicDataBean('23', '6', Data_P_10_2023.month_6))
    basicList.push(new MonthBasicDataBean('23', '5', Data_P_10_2023.month_5))
    basicList.push(new MonthBasicDataBean('23', '4', Data_P_10_2023.month_4))

    return basicList;
  }

  // static month_12: string =
  static month_11: string =
    "\t31\t22.20\t\t1\t7122\t7086\n" +
    "\t1\t10.35\t\t-1\t7086\t7138\n" +
    "\t1\t21.10\t\t1\t7160\t7318\n" +
    "\t6\t9.20\t\t-1\t7298\t7342\n" +
    "\t6\t14.55\t\t1\t7352\t7434\n" +
    "/\t7\t11.15\t\t-1\t7434\t7332\n" +
    "\t8\t9.20\t\t-1\t7342\t7370\n" +
    "\t8\t11.05\t\t1\t7390\t7328\n" +
    "\t8\t22.30\t\t-1\t7346\t7368\n" +
    "\t9\t11.05\t\t-1\t7316\t7310\n" +
    "\t10\t9.10\t\t1\t7302\t7376\n" +
    "\t13\t11.15\t\t-1\t7350\t7428\n" +
    "*\t14\t9.20\t\t1\t7428\t7576\n" +
    "/\t14\t21.50\t\t-1\t7576\t7582\n" +
    "*\t14\t22.40\t\t1\t7582\t7562\n" +
    "*\t16\t13.35\t\t-1\t7562\t7450\n" +
    "/\t17\t9.20\t\t1\t7450\t7484\n" +
    "\t17\t21.10\t\t-1\t7484\t7482\n" +
    "\t20\t13.45\t\t1\t7506\t7484\n" +
    "\t21\t13.35\t\t-1\t7484\t7496\n" +
    "\t22\t9.10\t\t1\t7496\t7496\n" +
    "\t22\t22.50\t\t-1\t7458\t7344\n" +
    "\t27\t10.55\t\t-1\t7280\t7354\n" +
    "\t27\t14.55\t\t1\t7408\t7378\n" +
    "*\t28\t13.35\t\t-1\t7378\t7326\n" +
    "/\t28\t21.50\t\t1\t7326\t7398\n" +
    "\t29\t10.10\t\t-1\t7326\t7308"

  static month_10: string =
    "\t9\t21.50\t\t-1\t7156\t7070\n" +
    "\t10\t14.55\t\t1\t7078\t7066\n" +
    "\t11\t14.25\t\t-1\t7050\t7078\n" +
    "\t12\t10.10\t\t1\t7084\t7300\n" +
    "\t16\t22.40\t0\t-1\t7300\t7336\n" +
    "\t17\t21.40\t\t1\t7332\t7352\n" +
    "\t18\t21.10\t\t-1\t7352\t7394\n" +
    "\t19\t9.30\t\t1\t7394\t7344\n" +
    "\t19\t14.45\t\t-1\t7344\t7242\n" +
    "\t20\t23.00\t\t1\t7252\t7172\n" +
    "\t23.00\t9.20\t\t-1\t7172\t7180\n" +
    "\t24\t9.20\t\t-1\t7114\t7084\n" +
    "\t25\t9.10\t\t1\t7100\t7054\n" +
    "\t25\t10.35\t\t-1\t7024\t7054\n" +
    "*\t25\t21.40\t\t1\t7054\t7092\n" +
    "/\t26\t9.30\t\t-1\t7092\t7112\n" +
    "*\t26\t11.15\t\t1\t7112\t7162\n" +
    "\t27\t14.15\t\t1\t7244\t7246\n" +
    "\t30\t14.35\t\t-1\t7254\t7108"

  static month_9: string =
    "\t31\t23.00\t0\t-1\t7908\t7948\n" +
    "\t1\t11.05\t\t1\t7948\t7858\n" +
    "/\t1\t21.30\t\t1\t7862\t7814\n" +
    "\t4\t9.10\t\t-1\t7814\t7782\n" +
    "\t5\t9.40\t\t1\t7782\t7708\n" +
    "\t5\t10.35\t\t-1\t7708\t7776\n" +
    "/\t5\t22.10\t\t-1\t7778\t7778\n" +
    "\t6\t9.10\t\t1\t7778\t7726\n" +
    "\t6\t11.05\t\t-1\t7690\t7588\n" +
    "\t8\t14.15\t0\t1\t7588\t7638\n" +
    "*\t8\t21.10\t\t1\t7638\t7646\n" +
    "/\t8\t21.40\t\t-1\t7646\t7646\n" +
    "*\t8\t23.00\t\t1\t7646\t7516\n" +
    "\t11\t13.35\t\t-1\t7516\t7434\n" +
    "/\t11\t14.45\t\t1\t7434\t7464\n" +
    "\t12\t9.10\t\t-1\t7464\t7486\n" +
    "\t12\t10.10\t\t1\t7486\t7426\n" +
    "\t12\t13.35\t\t-1\t7426\t7494\n" +
    "\t13\t9.10\t\t1\t7494\t7374\n" +
    "\t13\t10.55\t0\t-1\t7374\t7362\n" +
    "\t13\t13.55\t1\t-1\t7362\t7470\n" +
    "\t14\t9.10\t0\t1\t7470\t7480\n" +
    "\t14\t13.35\t1\t1\t7480\t7484\n" +
    "\t15\t13.35\t\t-1\t7484\t7372\n" +
    "\t18\t21.40\t\t-1\t7324\t7318\n" +
    "\t19\t21.20\t\t1\t7318\t7278\n" +
    "\t20\t10.55\t0\t-1\t7278\t7330\n" +
    "\t20\t15.00\t\t1\t7330\t7300\n" +
    "\t20\t21.10\t\t-1\t7300\t7222\n" +
    "\t22\t9.30\t\t1\t7222\t7208\n" +
    "\t25\t14.05\t0\t-1\t7198\t7220\n" +
    "\t25\t21.40\t1\t-1\t7220\t7278\n" +
    "\t26\t9.40\t0\t1\t7278\t7280\n" +
    "\t26\t11.15\t1\t1\t7280\t7230\n" +
    "\t26\t21.40\t\t-1\t7244\t7292\n" +
    "\t27\t14.05\t\t1\t7292\t7468"

  static month_8: string =
    "\t1\t9.40\t\t-1\t7450\t7524\n" +
    "\t1\t11.15\t0\t1\t7524\t7542\n" +
    "\t1\t14.35\t1\t1\t7542\t7624\n" +
    "\t2\t22.40\t\t-1\t7624\t7574\n" +
    "\t4\t9.20\t0\t1\t7574\t7580\n" +
    "\t4\t11.15\t1\t1\t7580\t7512\n" +
    "\t4\t21.10\t\t-1\t7516\t7600\n" +
    "\t4\t23.00\t\t1\t7600\t7520\n" +
    "\t7\t11.25\t\t-1\t7520\t7384\n" +
    "\t9\t13.35\t\t1\t7406\t7434\n" +
    "\t10\t10.10\t\t-1\t7434\t7492\n" +
    "\t10\t22.30\t\t1\t7492\t7436\n" +
    "\t11\t10.35\t\t-1\t7408\t7404\n" +
    "\t11\t22.50\t\t1\t7404\t7376\n" +
    "\t14\t13.45\t\t1\t7370\t7410\n" +
    "\t15\t9.10\t\t1\t7410\t7578\n" +
    "\t16\t14.15\t\t1\t7612\t7576\n" +
    "\t17\t9.40\t0\t-1\t7576\t7618\n" +
    "\t17\t14.15\t\t1\t7648\t7672\n" +
    "\t18\t14.55\t\t-1\t7618\t7686\n" +
    "\t21\t9.20\t0\t1\t7686\t7664\n" +
    "\t21\t14.15\t\t1\t7664\t7656\n" +
    "\t21\t14.45\t\t-1\t7656\t7654\n" +
    "\t22\t21.30\t\t1\t7654\t7594\n" +
    "\t22\t22.40\t\t-1\t7594\t7588\n" +
    "\t23\t14.35\t\t-1\t7564\t7624\n" +
    "\t23\t22.10\t\t1\t7624\t7670\n" +
    "\t24\t14.45\t\t-1\t7670\t7756\n" +
    "\t25\t9.20\t0\t1\t7756\t7722\n" +
    "\t25\t10.45\t\t1\t7722\t7796\n" +
    "\t28\t13.45\t\t-1\t7764\t7774\n" +
    "\t29\t10.10\t\t1\t7806\t7776\n" +
    "\t30\t11.05\t0\t-1\t7776\t7812\n" +
    "\t30\t14.45\t\t-1\t7812\t7872\n" +
    "\t30\t21.20\t0\t1\t7872\t7858\n" +
    "\t31\t9.10\t\t1\t7858\t7876\n" +
    "\t31\t21.40\t0\t-1\t7854\t7904"

  static month_7: string =
    "\t30\t23.00\t\t1\t7494\t7626\n" +
    "\t3\t22.00\t0\t-1\t7604\t7618\n" +
    "\t4\t13.35\t1\t-1\t7618\t7622\n" +
    "\t4\t23.00\t\t1\t7630\t7594\n" +
    "\t5\t10.00\t\t1\t7594\t7594\n" +
    "\t6\t10.10\t\t1\t7544\t7586\n" +
    "\t6\t14.25\t\t1\t7586\t7510\n" +
    "\t6\t21.40\t\t-1\t7510\t7478\n" +
    "\t10\t14.38\t\t1\t7490\t7568\n" +
    "\t11\t13.55\t\t1\t7524\t7550\n" +
    "\t12\t10.35\t\t1\t7578\t7510\n" +
    "\t12\t21.35\t0\t1\t7598\t7536\n" +
    "\t13\t9.10\t0\t1\t7380\t7398\n" +
    "\t13\t11.15\t1\t1\t7398\t7504\n" +
    "\t13\t22.40\t\t1\t7486\t7478\n" +
    "\t14\t21.40\t\t-1\t7508\t7582\n" +
    "\t17\t10.45\t\t1\t7582\t7572\n" +
    "\t18\t21.10\t\t-1\t7578\t7676\n" +
    "\t19\t9.20\t0\t1\t7676\t7646\n" +
    "\t19\t11.15\t1\t1\t7646\t7744\n" +
    "\t21\t10.45\t\t1\t7768\t7724\n" +
    "\t21\t11.15\t\t-1\t7724\t7638\n" +
    "\t21\t22.10\t\t1\t7654\t7680\n" +
    "\t25\t22.40\t\t-1\t7680\t7738\n" +
    "\t26\t10.55\t\t1\t7738\t7784\n" +
    "\t27\t10.00\t\t-1\t7784\t7644\n" +
    "\t28\t21.40\t\t-1\t7592\t7478"

  static month_6: string =
    "\t1\t10.10\t\t1\t6352\t6376\n" +
    "\t2\t10.35\t\t1\t6414\t6508\n" +
    "\t5\t13.35\t\t-1\t6496\t6536\n" +
    "\t5\t21.40\t\t-1\t6486\t6546\n" +
    "\t7\t10.35\t\t1\t6528\t6470\n" +
    "\t7\t14.15\t\t-1\t6468\t6452\n" +
    "\t8\t22.50\t\t1\t6474\t6642\n" +
    "\t13\t10.10\t\t1\t6608\t6732\n" +
    "\t15\t9.10\t\t-1\t6744\t6794\n" +
    "\t15\t13.35\t\t1\t6794\t7166\n" +
    "\t19\t11.15\t\t1\t7246\t7186\n" +
    "\t20\t21.40\t\t-1\t7196\t7132\n" +
    "\t26\t14.25\t\t1\t7088\t7124\n" +
    "\t28\t10.35\t\t1\t7216\t7262\n" +
    "\t28\t22.20\t\t-1\t7236\t7310\n" +
    "\t29\t14.15\t\t1\t7324\t7370\n" +
    "\t30\t21.20\t\t1\t7400\t7494"

  static month_5: string =
    "\t4\t9.00\t\t-1\t6846\t6898\n" +
    "\t4\t14.15\t\t1\t6896\t7174\n" +
    "\t9\t14.15\t\t-1\t7166\t7220\n" +
    "\t10\t14.55\t\t-1\t7118\t6914\n" +
    "\t12\t22.50\t\t-1\t6846\t6878\n" +
    "\t15\t10.45\t\t-1\t6812\t6878\n" +
    "\t15\t14.35\t\t1\t6884\t6848\n" +
    "\t16\t10.10\t\t-1\t6848\t6770\n" +
    "\t18\t10.45\t\t-1\t6722\t6746\n" +
    "\t18\t13.35\t\t-1\t6710\t6746\n" +
    "\t22\t9.10\t\t-1\t6696\t6684\n" +
    "\t22\t21.40\t\t1\t6684\t6654\n" +
    "\t22\t14.35\t\t-1\t6680\t6704\n" +
    "\t24\t9.20\t\t1\t6704\t6626\n" +
    "\t24\t22.30\t\t-1\t6610\t6684\n" +
    "\t25\t10.35\t\t1\t6684\t6772\n" +
    "\t26\t21.10\t\t-1\t6772\t6348"

  static month_4: string =
    "\t3\t\t\t1\t7788\t7798\n" +
    "\t\t\t\t1\t7866\t7832\n" +
    "\t\t\t\t-1\t7832\t7844\n" +
    "\t\t\t\t1\t7844\t7764\n" +
    "\t\t\t\t-1\t7764\t7684\n" +
    "\t\t\t\t1\t7746\t7868\n" +
    "\t\t\t\t-1\t7868\t7688\n" +
    "\t\t\t\t1\t7738\t7666\n" +
    "\t\t\t\t-1\t7648\t7650\n" +
    "\t\t\t\t1\t7130\t7370\n" +
    "\t\t\t\t-1\t7370\t7126\n" +
    "\t\t\t\t-1\t7074\t7076\n" +
    "\t\t\t\t1\t7076\t7012\n" +
    "\t\t\t\t-1\t7000\t7024\n" +
    "\t\t\t\t1\t7058\t7020\n" +
    "\t\t\t\t-1\t7020\t7056\n" +
    "\t\t\t\t1\t7056\t6990\n" +
    "\t\t\t\t-1\t6990\t6846"

  static month_3: string =
    "\t\t\t\t1\t8222\t8388\n" +
    "\t\t\t\t-1\t8340\t8254\n" +
    "\t\t\t\t-1\t8154\t8080\n" +
    "\t\t\t\t-1\t8032\t8070\n" +
    "\t\t\t\t-1\t7856\t7820\n" +
    "\t\t\t\t1\t7820\t7830\n" +
    "\t\t\t\t-1\t7766\t7742\n" +
    "\t\t\t\t-1\t7708\t7766\n" +
    "\t\t\t\t1\t7852\t7808\n" +
    "\t\t\t\t-1\t7702\t7698\n" +
    "\t\t\t\t1\t7722\t7658\n" +
    "\t\t\t\t-1\t7604\t7226\n" +
    "\t24\t\t\t1\t7252\t7260\n" +
    "\t\t\t\t-1\t7242\t7332\n" +
    "\t\t\t\t1\t7332\t7512\n" +
    "\t\t\t\t-1\t7512\t7520\n" +
    "\t\t\t\t1\t7544\t7568\n" +
    "\t\t\t\t-1\t7568\t7662"

  static month_2: string =
    "\t\t\t\t1\t7672\t7784\n" +
    "\t\t\t\t1\t7848\t8014\n" +
    "\t\t\t\t-1\t7990\t8018\n" +
    "\t\t\t\t-1\t7912\t7888\n" +
    "\t\t\t\t1\t7946\t7848\n" +
    "\t\t\t\t-1\t7848\t7868\n" +
    "\t\t\t\t1\t7868\t7878\n" +
    "\t\t\t\t1\t7946\t7870\n" +
    "\t\t\t\t-1\t7870\t7902\n" +
    "\t\t\t\t1\t7926\t8200\n" +
    "\t\t\t\t-1\t8200\t8224\n" +
    "\t\t\t\t1\t8224\t8258\n" +
    "\t\t\t\t-1\t8226\t8294\n" +
    "\t\t\t\t1\t8316\t8276\n" +
    "\t\t\t\t-1\t8276\t8274\n" +
    "\t\t\t\t-1\t8188\t8222"
}