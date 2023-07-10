# 导入 efinance 库
import efinance as ef
import pandas as pd
import datetime

#获取前一天的日期
def getTwoDayBeforeDate():
    today = datetime.datetime.today()
    oneDay = datetime.timedelta(days=1)
    return (today-oneDay).strftime("%Y%m%d")

# print(getTwoDayBeforeDate())

# 燃油2309,113.fu2309
df = ef.futures.get_quote_history(quote_ids='113.fu2309', beg='20230101', end='20500101', klt=101)
df.to_csv('燃油2309_日K.csv', index=None, encoding='utf-8-sig')

# 热卷2310 - 113.hc2310 日K线
# df = ef.futures.get_quote_history(quote_ids='113.hc2310', beg='20230101', end='20500101', klt=101)
# df.to_csv('热卷2310_日K.csv', index=None, encoding='utf-8-sig')

# 螺纹钢2310-113.rb2310 日K线
# df = ef.futures.get_quote_history(quote_ids='113.rb2310', beg='20230101', end='20500101', klt=101)
# df.to_csv('螺纹钢2310_日K.csv', index=None, encoding='utf-8-sig')

# """
# 菜油-OI309 日K线
# df = ef.futures.get_quote_history(quote_ids='115.OI309', beg='20230101', end='20500101', klt=101)
# df.to_csv('菜油2309_日K.csv', index=None, encoding='utf-8-sig')

# 菜油-OI309 时K线
# df = ef.futures.get_quote_history(quote_ids='115.OI309', beg=getTwoDayBeforeDate(), end='20500101', klt=60)
# df.to_csv('菜油2309_时K.csv', index=None, encoding='utf-8-sig')

# 菜油-OI309 5K线
df = ef.futures.get_quote_history(quote_ids='115.OI309', beg=getTwoDayBeforeDate(), end='20500101', klt=5)
df.to_csv('菜油2309_5K.csv', index=None, encoding='utf-8-sig')

# 菜油-OI309 日K线
# df = ef.futures.get_quote_history(quote_ids='115.OI309', beg='20230101', end='20500101', klt=101)
# df.to_csv('菜油2309_日K.csv', index=None, encoding='utf-8-sig')

# 棕榈油-p2309 时K线
# df = ef.futures.get_quote_history(quote_ids='114.p2309', beg=getTwoDayBeforeDate(), end='20500101', klt=60)
# df.to_csv('棕榈油2309_时K.csv', index=None, encoding='utf-8-sig')

# 棕榈油-p2309 5K线
df = ef.futures.get_quote_history(quote_ids='114.p2309', beg=getTwoDayBeforeDate(), end='20500101', klt=5)
df.to_csv('棕榈油2309_5K.csv', index=None, encoding='utf-8-sig')
# """


# df.to_csv('菜油2309_时K.csv', index=None, encoding='utf-8-sig')





