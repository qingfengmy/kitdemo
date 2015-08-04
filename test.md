```
AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
Intent intent1 = new Intent();
intent1.setAction("cn.mchina.wfenxiao.action.infox");
Calendar c = Calendar.getInstance();
c.set(Calendar.HOUR_OF_DAY, 23);
c.set(Calendar.MINUTE, 50);
c.set(Calendar.SECOND, 0);
LogUtil.d("wfenxiao", "--" + c.getTimeInMillis());
PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent1, 0);
// RTC_WAKEUP闹钟在睡眠状态下可用
am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 60 * 1000, pi);

Intent intent2 = new Intent();
intent2.setAction("cn.mchina.wfenxiao.action.infox");
c.set(Calendar.HOUR_OF_DAY, 23);
c.set(Calendar.MINUTE, 55);
c.set(Calendar.SECOND, 0);
LogUtil.d("wfenxiao", "--" + c.getTimeInMillis());
pi = PendingIntent.getBroadcast(this, 1, intent2, 0);
am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 60 * 1000, pi);
```
