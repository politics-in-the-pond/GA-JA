package gachon.termproject.gaja.login;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import gachon.termproject.gaja.Info.PostInfo;
import gachon.termproject.gaja.R;
import gachon.termproject.gaja.post.PostInformationActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    RandomNumberGenerator rng = new RandomNumberGenerator();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) { //알림 리스트 추가 여기서 해주세요
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotificationFromPost(remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, SplashActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("GA-JA")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotificationFromPost(Map messagebody){
        if(!messagebody.get("postno").equals("")){

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("posts").document(messagebody.get("postno").toString()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = null;
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        PostInfo postInfo = new PostInfo(
                                document.getData().get("titleImage").toString(),
                                document.getData().get("title").toString(),
                                document.getData().get("content").toString(),
                                document.getData().get("publisher").toString(),
                                document.getData().get("userName").toString(),
                                new Date(document.getDate("createdAt").getTime()),
                                (Long) document.getData().get("peopleNeed"),
                                (Long) document.getData().get("currentNumOfPeople"),
                                document.getData().get("postId").toString(),
                                (ArrayList<String>) document.getData().get("participatingUserId"),
                                document.getData().get("category").toString(),
                                new Date(document.getDate("finishTime").getTime()),
                                document.getData().get("talkLink").toString()
                        );

                        Intent intent = new Intent(this, PostInformationActivity.class);
                        intent.putExtra("PostInfo",postInfo);
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            manager.createNotificationChannel(new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT));
                            builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
                        } else{
                            builder = new NotificationCompat.Builder(this);
                        }

                        builder.setContentTitle("GA-JA").setContentText((String) messagebody.get("title") + " : " +(String) messagebody.get("message") + " 글의 인원모집이 완료되었습니다!").setSmallIcon(R.mipmap.ic_launcher).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                        Notification notification = builder.build();
                        manager.notify((int) rng.MT19937_long(Seed.MakeSeed(messagebody.get("postno").toString())), notification);
                        AlarmIOProvider alarmIOProvider = new AlarmIOProvider();
                        alarmIOProvider.AppendAlarm(getString(R.string.alarm) + " " + messagebody.get("title"));
                    }
                } else{
                    //SendFull(postInfo);
                }
            });
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String deviceToken = s;
        // Do whatever you want with your token now
        // i.e. store it on SharedPreferences or DB
        // or directly send it to server
    }
}
