package com.bitcoin.linyujun;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.bitcoin.linyujun.base.CoinTypes;
import com.bitcoin.linyujun.base.ECKeyPair;
import com.bitcoin.linyujun.bip.bip32.ExtendedKey;
import com.bitcoin.linyujun.bip.bip39.MnemonicGenerator;
import com.bitcoin.linyujun.bip.bip39.RandomSeed;
import com.bitcoin.linyujun.bip.bip39.SeedCalculator;
import com.bitcoin.linyujun.bip.bip39.WordCount;
import com.bitcoin.linyujun.bip.bip39.wordlists.English;
import com.bitcoin.linyujun.bip.bip44.AddressIndex;
import com.bitcoin.linyujun.bip.bip44.BIP44;
import com.bitcoin.linyujun.bip.bip44.CoinPairDerive;

import java.util.List;


public class KeepAliveService extends IntentService {

    private static final int NOTIFICATION_ID = 10008;

    private long count = 0;

    private List<String> keyList;

    private LocalBroadcastManager mLocalBroadcastManager;

    public KeepAliveService() {
        super("KeepAliveService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        Notification fadeNotification = getNotificationWidget(this);
        startForeground(NOTIFICATION_ID, fadeNotification);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            keyList = intent.getStringArrayListExtra("keys");
            if (keyList == null || keyList.size() == 0) {
                sendThreadStatus("count", "数据错误");
                return;
            }
            while (!MainActivity.stop) {
                if (count % 10 == 0) {
                    sendThreadStatus("count", "已经执行了：" + count + "次");
                }
                count++;
                byte[] random = RandomSeed.random(WordCount.TWELVE);
                final List<String> mnemonics = new MnemonicGenerator(English.INSTANCE).createMnemonic(random);

                byte[] seed = new SeedCalculator().calculateSeed(mnemonics, "");
                ExtendedKey extendedKey = ExtendedKey.create(seed);

                AddressIndex addressIndex = BIP44.m().purpose44()
                        .coinType(CoinTypes.Bitcoin)
                        .account(0)
                        .external()
                        .address(0);

                ECKeyPair childKeyPair = new CoinPairDerive(extendedKey).derive(addressIndex);

                String address = childKeyPair.getAddress();

                for (final String key : keyList) {
                    if (address.startsWith(1 + key)) {

                        StringBuilder mne = new StringBuilder();
                        for (String str : mnemonics) {
                            mne.append(str).append(" ");
                        }

                        com.bitcoin.linyujun.FileUtils.writeTxtToFile("tomato.txt",
                                key + " : " + address + "\n" + childKeyPair.getPrivateKey() + "\n" + mne.toString() + "\n");

                        sendThreadStatus("res", "已经找到 '" + key + "'的靓号，已保存在sd卡根目录下tomato.txt文件中");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 发送进度消息
     */
    private void sendThreadStatus(String key , String value) {
        Intent intent = new Intent(MainActivity.ACTION_RES );
        intent.putExtra("key", key);
        intent.putExtra(key, value);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    public static Notification getNotificationWidget(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent pi = PendingIntent.getActivity(context, 12342, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pi);
        // 隐藏顶部通知图标
        notificationBuilder.setPriority(Notification.PRIORITY_MIN);

        // 数据获取
        notificationBuilder.setContentTitle("正在努力计算靓号...");

        return notificationBuilder.build();
    }
}
