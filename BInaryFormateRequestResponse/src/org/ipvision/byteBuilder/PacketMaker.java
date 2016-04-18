package org.ipvision.byteBuilder;

import org.ipvision.attribute.code.Code;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.ringid.contacts.ContactListDTO;
import org.ringid.feedbacks.ContactFeedBack;
import org.ringid.feedbacks.FeedBack;
import org.ringid.feedbacks.UserAuthFeedBack;
import org.ringid.feedbacks.UserDetailsFeedBack;
import org.ringid.receiverparams.AuthParameters;
import org.ringid.receiverparams.CallParameters;
import org.ringid.receiverparams.ChatParameters;
import org.ringid.receiverparams.RequestParameters;
import org.ringid.users.UserDTO;
import java.lang.reflect.Type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author saikat
 */
public class PacketMaker {

    private String request;

    public ArrayList<byte[]> getListOfPackets() {

        //RequestParameters requestParameters = new RequestParameters();
        Gson gson = new Gson();

        ArrayList<byte[]> listOfPackets = null;

        System.out.println(this.request);

        //System.out.println("ACtion " + authParameters.getAction() + " Client Packet ID " + authParameters.getPacketId());
        JsonReader reader = new JsonReader(new StringReader(this.request));
        reader.setLenient(true);

        JsonObject requestObject = gson.fromJson(reader, JsonObject.class);

        int totalHeaderSize = 0;

        int action = requestObject.get("actn").getAsInt();

        switch (action) {
            /**
             * Sign in response
             */
            case 20: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                UserAuthFeedBack userAuthFeedBack = new UserAuthFeedBack();
                userAuthFeedBack.setIsEmailVerified(0);
                userAuthFeedBack.setOfflineIP("38.127.68.55");
                userAuthFeedBack.setMood(1);
                userAuthFeedBack.setProfileImageId(0);
                userAuthFeedBack.setUserIdentity(authParameters.getUserIdentity());
//            userAuthFeedBack.setSessionID(String.valueOf((new Random().nextInt()) % 100000));
                userAuthFeedBack.setSessionID("abaaadsfdf");
                userAuthFeedBack.setOfflinePort(1246);
                userAuthFeedBack.setProfileImage("");
                userAuthFeedBack.setSuccess(true);
                userAuthFeedBack.setPassword(authParameters.getPassword());
                userAuthFeedBack.setIsMyNumberVerified(0);
                userAuthFeedBack.setLiveStatus(2);
                userAuthFeedBack.setEmoticonVersion(0);
                userAuthFeedBack.setLastOnlineTime(new Date().getTime());
                userAuthFeedBack.setIsPasswordSeted(true);
                userAuthFeedBack.setFullName("Messi");
                userAuthFeedBack.setUserTableID(52349);

                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = userAuthFeedBack.getSessionId().getBytes().length;
                // System.out.println("Session Byte size " + sessionIdBytes);

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + sessionIdBytes + clientPacketIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .addString(Code.SESSION_ID, userAuthFeedBack.getSessionId())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addInt(Code.IS_EMAIL_VERIFIED, userAuthFeedBack.getIsEmailVerified(), 2)
                        .addString(Code.OFFLINE_SERVER_IP, userAuthFeedBack.getOfflineIP())
                        .addInt(Code.MOOD, userAuthFeedBack.getMood(), 2)
                        .addLong(Code.PROFILE_IMAGE_ID, userAuthFeedBack.getProfileImageId(), 6)
                        .addString(Code.USER_IDENTITY, userAuthFeedBack.getUserIdentity())
                        .addInt(Code.OFFLINE_SERVER_PORT, userAuthFeedBack.getOfflinePort(), 4)
                        .addString(Code.PROFILE_IMAGE, userAuthFeedBack.getProfileImage())
                        .addBool(Code.SUCCESS, userAuthFeedBack.isSuccess())
                        .addString(Code.PASSWORD, userAuthFeedBack.getPassword())
                        .addInt(Code.IS_MY_NUMBER_VERIFIED, userAuthFeedBack.getIsEmailVerified(), 1)
                        .addInt(Code.LIVE_STATUS, userAuthFeedBack.getLiveStatus(), 2)
                        .addDouble(Code.EMOTICON_VERSION, userAuthFeedBack.getEmoticonVersion())
                        .addLong(Code.LAST_ONLINE_TIME, userAuthFeedBack.getLastOnlineTime(), 8)
                        .addBool(Code.PASSWORD_SETED, userAuthFeedBack.getIsPasswordSeted())
                        .addString(Code.USER_NAME, userAuthFeedBack.getFullName())
                        .addLong(Code.USER_ID, userAuthFeedBack.getUserTableID(), 8)
                        .build();
                break;
            }
            /**
             *
             * Email verification code request & verification confirmation
             * response
             */
            case 220: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                JsonObject jsonObject = gson.fromJson(this.request, JsonObject.class);

                UserAuthFeedBack userAuthFeedBack = new UserAuthFeedBack();

                String authParamString = gson.toJson(authParameters);

                System.out.println(authParamString);

                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                byte[] header = null;

                listOfPackets = null;

                //System.err.println(authParameters.getEmailVerificationCode());
                totalHeaderSize = 2 * (Type + Length) + actionBytes + clientPacketIdBytes;

                header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .getHeader();

                printHeaderBytes(header);

                String verificationCode = jsonObject.get("evc").getAsString();

                if (verificationCode != null) {

                    String emailVerification = "1000";

                    if (!verificationCode.equals(emailVerification)) {

                        userAuthFeedBack.setSuccess(false);
                        userAuthFeedBack.setMessage("Verification code failed. Please Try again");
                        userAuthFeedBack.setReasonCode(0);

                        listOfPackets = new ByteBuilder()
                                .setHeader(header)
                                .addBool(Code.SUCCESS, userAuthFeedBack.isSuccess())
                                .addString(Code.MESSAGE, userAuthFeedBack.getMessage())
                                .addString(Code.EMAIL_VERIFICATION_CODE, verificationCode)
                                .addInt(Code.REASON_CODE, userAuthFeedBack.getReasonCode(), 4)
                                .build();

                    } else {

                        userAuthFeedBack.setSuccess(true);
                        userAuthFeedBack.setReasonCode(0);

                        listOfPackets = new ByteBuilder()
                                .setHeader(header)
                                .addBool(Code.SUCCESS, userAuthFeedBack.isSuccess())
                                .addString(Code.EMAIL_VERIFICATION_CODE, verificationCode)
                                .addInt(Code.REASON_CODE, userAuthFeedBack.getReasonCode(), 4)
                                .build();

                    }

                } else {

                    userAuthFeedBack.setSuccess(true);
                    userAuthFeedBack.setMessage("Verification is sent to your mobile");
                    userAuthFeedBack.setReasonCode(28);

                    listOfPackets = new ByteBuilder()
                            .setHeader(header)
                            .addBool(Code.SUCCESS, userAuthFeedBack.isSuccess())
                            .addString(Code.MESSAGE, userAuthFeedBack.getMessage())
                            .addInt(Code.REASON_CODE, userAuthFeedBack.getReasonCode(), 4)
                            .build();
                }

                break;

            }
            /**
             *
             * User update request responses
             */
            case 126: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);
                String responseString = "{\"sucs\":true,\"dwnMnd\":false,\"uId\":\"2110077639\",\"nm\":\"Neymar\",\"uIdChng\":false,\"uf\":1,\"pstd\":false,\"usrPw\":\"123456\",\"rc\":0}";
                UserAuthFeedBack userAuthFeedBack = new Gson().fromJson(responseString, UserAuthFeedBack.class);

                String sessionId = UUID.randomUUID().toString();
                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionId.length();
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.SESSION_ID, sessionId)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addBool(Code.DOWNLOAD_MANDATORY, userAuthFeedBack.getDownloadMandatory())
                        .addString(Code.USER_IDENTITY, userAuthFeedBack.getUserIdentity())
                        .addBool(Code.SUCCESS, userAuthFeedBack.isSuccess())
                        .addString(Code.PASSWORD, userAuthFeedBack.getPassword())
                        .addString(Code.USER_NAME, userAuthFeedBack.getName())
                        .addBool(Code.USER_ID_CHANGE, userAuthFeedBack.getUserIdChanged())
                        .addInt(Code.USER_FOUND, userAuthFeedBack.getUserFound(), 4)
                        .addBool(Code.PASSWORD_SETED, userAuthFeedBack.getIsPasswordSeted())
                        .addInt(Code.REASON_CODE, userAuthFeedBack.getReasonCode(), 2)
                        .build();

                break;

            }

            /**
             *
             * Add friend response
             */
            case Code.TYPE_ADD_FRIEND: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                String responseString = "{\"sucs\":true,\"uId\":\"2110010016\",\"fn\":\"A 10016\",\"gr\":\"\",\"frnS\":3,\"prIm\":\"cloud/uploaded-136/2110010016/4176251453118517972.jpg\",\"prImId\":9928,\"utId\":9,    \"ct\":2,\"nmf\":2,\"mb\":0,\"ut\":1457422058660,\"cla\":1,\"chta\":1,\"fda\":1}";
                ContactFeedBack contactFeedBack = new Gson().fromJson(responseString, ContactFeedBack.class);

                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = authParameters.getSessionId().length();

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .addString(Code.SESSION_ID, authParameters.getSessionId())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addString(Code.USER_IDENTITY, authParameters.getUserIdentity())
                        .addBool(Code.SUCCESS, contactFeedBack.isSuccess())
                        .addString(Code.USER_NAME, contactFeedBack.getFullName())
                        .addString(Code.GENDER, contactFeedBack.getGender())
                        .addString(Code.PROFILE_IMAGE, contactFeedBack.getProfileImage())
                        .addLong(Code.PROFILE_IMAGE_ID, contactFeedBack.getProfileImageId(), 8)
                        .addLong(Code.USER_ID, contactFeedBack.getUserTableID(), 8)
                        .addInt(Code.CONTACT_TYPE, contactFeedBack.getContactType(), 4)
                        .addInt(Code.MUTUAL_FRIEND_COUNT, contactFeedBack.getNoOfMutualFriends(), 4)
                        .addInt(Code.MATCH_BY, contactFeedBack.getMatchedBy(), 4)
                        .addLong(Code.UPDATE_TIME, contactFeedBack.getUpdateTime(), 8)
                        .addInt(Code.CALL_ACCESS, contactFeedBack.getCallAccess(), 4)
                        .addInt(Code.CHAT_ACCESS, contactFeedBack.getChatAccess(), 4)
                        .addInt(Code.FEED_ACCESS, contactFeedBack.getFeedAccess(), 4)
                        .build();

                break;
            }

            case Code.TYPE_DELETE_FRIEND: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                String responseString = "{\"uId\":\"2110067045\",\"utId\":52559,\"sucs\":true,\"rc\":0}";
                ContactFeedBack contactFeedBack = new Gson().fromJson(responseString, ContactFeedBack.class);

                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = authParameters.getSessionId().length();

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .addString(Code.SESSION_ID, authParameters.getSessionId())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addString(Code.USER_IDENTITY, authParameters.getUserIdentity())
                        .addBool(Code.SUCCESS, contactFeedBack.isSuccess())
                        .addLong(Code.USER_ID, contactFeedBack.getUserTableID(), 8)
                        .addInt(Code.REASON_CODE, contactFeedBack.getReasonCode(), 4)
                        .build();

                break;

            }

            case Code.TYPE_ACCEPT_FRIEND: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                String responseString = "{\"sucs\":true,\"uId\":\"2110067045\",\"fn\":\"Tuhin\",\"gr\":\"\",\"frnS\":1,\"prIm\":\"\",\"prImId\":0,\"utId\":52559,\"ct\":2,\"nmf\":6,\"ut\":1457422535496,\"cla\":1,\"chta\":1,\"fda\":1}";
                ContactFeedBack contactFeedBack = new Gson().fromJson(responseString, ContactFeedBack.class);

                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = authParameters.getSessionId().length();

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .addString(Code.SESSION_ID, authParameters.getSessionId())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addString(Code.USER_IDENTITY, contactFeedBack.getUserIdentity())
                        .addBool(Code.SUCCESS, contactFeedBack.isSuccess())
                        .addString(Code.USER_NAME, contactFeedBack.getFullName())
                        .addString(Code.GENDER, contactFeedBack.getGender())
                        .addInt(Code.FRIENDSHIP_STATUS, contactFeedBack.getFriendShipStatus(), 4)
                        .addString(Code.PROFILE_IMAGE, contactFeedBack.getProfileImage())
                        .addLong(Code.PROFILE_IMAGE_ID, contactFeedBack.getProfileImageId(), 8)
                        .addLong(Code.USER_ID, contactFeedBack.getUserTableID(), 8)
                        .addInt(Code.CONTACT_TYPE, contactFeedBack.getContactType(), 4)
                        .addInt(Code.MUTUAL_FRIEND_COUNT, contactFeedBack.getNoOfMutualFriends(), 4)
                        .addLong(Code.UPDATE_TIME, contactFeedBack.getUpdateTime(), 8)
                        .addInt(Code.CALL_ACCESS, contactFeedBack.getCallAccess(), 4)
                        .addInt(Code.CHAT_ACCESS, contactFeedBack.getChatAccess(), 4)
                        .addInt(Code.FEED_ACCESS, contactFeedBack.getFeedAccess(), 4)
                        .build();

                break;

            }

            case Code.TYPE_ACTION_UPDATE_CONTACT_ACCESS: {

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                String responseString = "{\"utId\":52349,\"sucs\":true,\"sn\":6,\"sv\":0,\"rc\":0}";
                FeedBack feedBack = new Gson().fromJson(responseString, FeedBack.class);

                JsonObject jsonObject = gson.fromJson(this.request, JsonObject.class);

                feedBack.setSettingsName(jsonObject.get("sn").getAsInt());
                feedBack.setSettingsValue(jsonObject.get("sv").getAsInt());
                feedBack.setUserTableID(jsonObject.get("utId").getAsLong());

                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = authParameters.getSessionId().length();

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .addString(Code.SESSION_ID, authParameters.getSessionId())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addBool(Code.SUCCESS, feedBack.isSuccess())
                        .addInt(Code.SETTINGS_NAME, feedBack.getSettingsName(), 4)
                        .addInt(Code.SETTINGS_VALUE, feedBack.getSettingsValue(), 4)
                        .addLong(Code.USER_ID, feedBack.getUserTableID(), 8)
                        .addInt(Code.REASON_CODE, feedBack.getReasonCode(), 4)
                        .build();

                break;
            }

            case Code.TYPE_SEND_REGISTER: {

                String responseString = "{\"fndId\":\"2110067045\",\"swIp\":\"38.127.68.57\",\"swPr\":1250,\"sucs\":true,\"psnc\":2,\"mood\":1,\"dvc\":2,\"callID\":\"jCRGryWt14608826592710249\",\"tm\":5688689304929771,\"dt\":\"dG6hFdvYraw:APA91bG0e23l164jp0liUyj9DPvoFUdT4419JvGmroyhgMHWcFnugpspvAyyzyjNAQkccDkg2cRs_LlostHH7myOBioOYyq0C6crpuGMgr-kXZ50mK63FamOY1QHECo_xMRzOP-H7ecZ\",\"fn\":\"Tuhin\",\"idc\":false,\"apt\":1,\"calT\":1,\"p2p\":2,\"rpt\":1}";
                // FeedBack feedBack = new Gson().fromJson(responseString, FeedBack.class);

                JsonObject jsonObject = gson.fromJson(this.request, JsonObject.class);

                CallParameters callParameters = gson.fromJson(responseString, CallParameters.class);

                callParameters.setAction(jsonObject.get("actn").getAsInt());
                callParameters.setFriendId(jsonObject.get("fndId").getAsLong());
                callParameters.setSessionId(jsonObject.get("sId").getAsString());
                callParameters.setPacketId(jsonObject.get("pckId").getAsString());
                callParameters.setCallID(jsonObject.get("callID").getAsString());

                if (callParameters.getMessage() == null) {
                    callParameters.setMessage("");
                }
                if (callParameters.getDeviceToken() == null) {
                    callParameters.setDeviceToken("");
                }

                // String jsonResponse  = gson.toJson(userAuthFeedBack);
                // System.out.println(jsonResponse);
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = callParameters.getSessionId().length();

                int clientPacketIdBytes = callParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, callParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, callParameters.getPacketId())
                        .addString(Code.SESSION_ID, callParameters.getSessionId())
                        .getHeader();

                printHeaderBytes(header);

                System.out.println(callParameters.getSwitchIp());

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addLong(Code.FRIEND_ID, callParameters.getFriendId(), 8)
                        .addString(Code.SWITCH_IP, callParameters.getSwitchIp())
                        .addInt(Code.SWITCH_PORT, callParameters.getSwitchPort(), 4)
                        .addBool(Code.SUCCESS, callParameters.isSuccess())
                        .addInt(Code.PRESENCE, callParameters.getPresence(), 4)
                        .addInt(Code.MOOD, callParameters.getMood(), 4)
                        .addInt(Code.DEVICE, callParameters.getDevice(), 4)
                        .addString(Code.CALL_ID, callParameters.getCallID())
                        .addLong(Code.CALL_TIME, callParameters.getCallTime(), 8)
                        .addString(Code.DEVICE_TOKEN, callParameters.getDeviceToken())
                        .addString(Code.USER_NAME, callParameters.getFullName())
                        .addBool(Code.IS_DIVERTED_CALL, callParameters.getIsDivertedCall())
                        .addInt(Code.APPLICATION_TYPE, callParameters.getAppType(), 4)
                        .addInt(Code.P2P_STATUS, callParameters.getP2pCallStatus(), 4)
                        .addInt(Code.REMOTE_PUSH_TYPE, callParameters.getRemotePushType(), 4)
                        .addString(Code.MESSAGE, callParameters.getMessage())
                        .build();

                break;
            }

            case Code.TYPE_ACTION_STORE_CONTACT_LIST: {

                break;
            }

            case Code.ACTION_USER_SHORT_DETAILS: {

                String responseString = "{\"sucs\":true,\"userDetails\":{\"uId\":\"2110033856\",\"fn\":\"Bptpzsbcub\",\"prIm\":\"xxxxx\",\"cIm\":\"yyyyy\",\"prImId\":0,\"prImPr\":1,\"utId\":22061,\"ispc\":1,\"isepc\":1,\"frnS\":1,\"fda\":1,\"cla\":1,\"chta\":1},\"utId\":22061}";
                // FeedBack feedBack = new Gson().fromJson(responseString, FeedBack.class);
                JsonReader responseReader = new JsonReader(new StringReader(responseString));
                responseReader.setLenient(true);

                JsonObject responseObject = gson.fromJson(responseReader, JsonObject.class);

                UserDetailsFeedBack userDetailsFeedBack = gson.fromJson(responseObject, UserDetailsFeedBack.class);

                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = requestObject.get("sId").getAsString().getBytes().length;

                int clientPacketIdBytes = requestObject.get("pckId").getAsString().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, action, 4)
                        .addString(Code.CLIENT_PACKET_ID, requestObject.get("pckId").getAsString())
                        .addString(Code.SESSION_ID, requestObject.get("sId").getAsString())
                        .getHeader();

                printHeaderBytes(header);

                UserDTO userDTO = gson.fromJson(responseObject.get("userDetails").getAsJsonObject(), UserDTO.class);
                System.out.println(userDTO.getUserIdentity());
                byte[] userDTObytes = new ListByteBuilder()
                        .addString(Code.USER_IDENTITY, userDTO.getUserIdentity())
                        .addString(Code.USER_NAME, userDTO.getFullName())
                        .addString(Code.PROFILE_IMAGE, userDTO.getProfileImage())
                        .addLong(Code.PROFILE_IMAGE_ID, userDTO.getProfileImageId(), 8)
                        .addLong(Code.USER_ID, userDTO.getUserTableID(), 8)
                        .addInt(Code.IS_PICKED_FROM_PHONE, userDTO.getIsNumberPicked(), 4)
                        .addInt(Code.IS_PICKED_EMAIL_FROM_PHONE, userDTO.getIsEmailPicked(), 4)
                        .addInt(Code.FRIENDSHIP_STATUS, userDTO.getFriendShipStatus(), 2)
                        .addInt(Code.FEED_ACCESS, userDTO.getFeedAccess(), 1)
                        .addInt(Code.CALL_ACCESS, userDTO.getCallAccess(), 1)
                        .addInt(Code.CHAT_ACCESS, userDTO.getChatAccess(), 1)
                        .getListBytes();

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addBool(Code.SUCCESS, userDetailsFeedBack.isSuccess())
                        .addByte(Code.USER_DETAILS, userDTObytes)
                        .addLong(Code.USER_ID, userDTO.getUserTableID(), 8)
                        .build();

                break;

            }

            case Code.ACTION_BLOCK_UNBLOCK_FRIEND: {

                String responseString = "{\"sucs\":true,\"bv\":1,\"rc\":0,\"idList\":[52349],\"cla\":1,\"chta\":1,\"fda\":1}";

                FeedBack feedBack = gson.fromJson(responseString, FeedBack.class);

                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = requestObject.get("sId").getAsString().getBytes().length;

                int clientPacketIdBytes = requestObject.get("pckId").getAsString().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, action, 4)
                        .addString(Code.CLIENT_PACKET_ID, requestObject.get("pckId").getAsString())
                        .addString(Code.SESSION_ID, requestObject.get("sId").getAsString())
                        .getHeader();

                printHeaderBytes(header);

                ArrayList<Long> idList = feedBack.getUserIds();

                listOfPackets = new ArrayList<>();

                ByteBuilder byteBuilder = new ByteBuilder()
                        .setHeader(header)
                        .addBool(Code.SUCCESS, feedBack.isSuccess())
                        .addInt(Code.BLOCK_VALUE, feedBack.getBlockValue(), 4)
                        .addInt(Code.REASON_CODE, feedBack.getReasonCode(), 4)
                        .addInt(Code.CALL_ACCESS, feedBack.getCallAccess(), 4)
                        .addInt(Code.CHAT_ACCESS, feedBack.getChatAccess(), 4)
                        .addInt(Code.FEED_ACCESS, feedBack.getFeedAccess(), 4);

                for (Long id : idList) {

                    System.out.println(id);
                    byteBuilder = byteBuilder.addLong(Code.USER_IDENTITY, id, 8);
                }

                listOfPackets = byteBuilder.build();

                break;

            }

            case Code.TYPE_CHECK_PRESENCE: {

                String responseString = "{\"fndId\":\"2110067045\",\"psnc\":3,\"dvc\":2,\"sucs\":true,\"lot\":1460868436099,\"mood\":1,\"isclb\":false}";

                ChatParameters chatParameters = gson.fromJson(responseString, ChatParameters.class);

                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = requestObject.get("sId").getAsString().getBytes().length;

                int clientPacketIdBytes = requestObject.get("pckId").getAsString().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, action, 4)
                        .addString(Code.CLIENT_PACKET_ID, requestObject.get("pckId").getAsString())
                        .addString(Code.SESSION_ID, requestObject.get("sId").getAsString())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addLong(Code.FRIEND_ID, Long.parseLong(chatParameters.getFriendIdentity()), 8)
                        .addBool(Code.SUCCESS, chatParameters.isSuccess())
                        .addInt(Code.PRESENCE, chatParameters.getPresence(), 4)
                        .addInt(Code.MOOD, chatParameters.getMood(), 4)
                        .addInt(Code.DEVICE, chatParameters.getDevice(), 4)
                        .addLong(Code.LAST_ONLINE_TIME, chatParameters.getLastOnlineTime(), 8)
                        .addBool(Code.IS_CELEBRITY, chatParameters.isIsCelebrity())
                        .build();

                break;

            }

            case Code.TYPE_START_FRIEND_CHAT: {

                String responseString = "{\"fndId\":\"2110067045\",\"sucs\":false,\"psnc\":3,\"dvc\":2,"
                        + "\"dt\":\"dG6hFdvYraw:APA91bG0e23l164jp0liUyj9DPvoFUdT4419JvGmroyhgMHWcFnugpspvAyyzyjNAQkccDkg2cRs_LlostHH7myOBioOYyq0C6crpuGMgr-kXZ50mK63FamOY1QHECo_xMRzOP-H7ecZ\","
                        + "\"lot\":1460868436099,\"nm\":\"Tuhin\",\"rc\":0,\"apt\":1,\"mood\":1,\"utId\":52559,\"isclb\":false,\"rpt\":1}";

                ChatParameters chatParameters = gson.fromJson(responseString, ChatParameters.class);

                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = requestObject.get("sId").getAsString().getBytes().length;

                int clientPacketIdBytes = requestObject.get("pckId").getAsString().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 3 * (Type + Length) + actionBytes + clientPacketIdBytes + sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, action, 4)
                        .addString(Code.CLIENT_PACKET_ID, requestObject.get("pckId").getAsString())
                        .addString(Code.SESSION_ID, requestObject.get("sId").getAsString())
                        .getHeader();

                printHeaderBytes(header);

                listOfPackets = new ArrayList<>();

                listOfPackets = new ByteBuilder()
                        .setHeader(header)
                        .addLong(Code.FRIEND_ID, Long.parseLong(chatParameters.getFriendIdentity()), 8)
                        .addBool(Code.SUCCESS, chatParameters.isSuccess())
                        .addInt(Code.PRESENCE, chatParameters.getPresence(), 4)
                        .addInt(Code.MOOD, chatParameters.getMood(), 4)
                        .addInt(Code.DEVICE, chatParameters.getDevice(), 4)
                        .addLong(Code.LAST_ONLINE_TIME, chatParameters.getLastOnlineTime(), 8)
                        .addBool(Code.IS_CELEBRITY, chatParameters.isIsCelebrity())
                        .addString(Code.DEVICE_TOKEN, chatParameters.getDeviceToken())
                        .addString(Code.FRIEND_NAME, chatParameters.getName())
                        .addInt(Code.APPLICATION_TYPE, chatParameters.getAppType(), 4)
                        .addLong(Code.USER_ID, chatParameters.getUserTableID(), 8)
                        .addInt(Code.REMOTE_PUSH_TYPE, chatParameters.getRemotePushType(), 4)
                        .addInt(Code.REASON_CODE, chatParameters.getReasonCode(), 4)
                        .build();

                break;
            }

            case Code.ACTION_USERS_DETAILS: {

                String responseString = "[{\"seq\":\"1/2\",\"contactList\":[{\"ringID\":2110010067,\"uId\":\"2110010067\",\"fn\":\"Wahid_Love_IPvision\",\"gr\":\"Male\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"cloud/uploaded-141/2110010067/4054441459979400103.jpg\",\"prImId\":13757,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":56,\"ut\":1460887625947,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":6,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110011243,\"uId\":\"2110011243\",\"fn\":\"Call Test\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"cloud/uploaded-139/2110011243/10139411457539176533.jpg\",\"prImId\":12981,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":642,\"ut\":1457521034826,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":2,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110067262,\"uId\":\"2110067262\",\"fn\":\"Salah Uddin\",\"gr\":\"Male\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"\",\"prImId\":0,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":55555,\"ut\":1455195888148,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":2,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110071649,\"uId\":\"2110071649\",\"fn\":\"Rajib Pal\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"\",\"prImId\":0,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":55545,\"ut\":1455290367739,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":0,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110066959,\"uId\":\"2110066959\",\"fn\":\"Mizan Test\",\"gr\":\"Male\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"\",\"prImId\":0,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":52548,\"ut\":1451209874994,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":0,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0}],\"sucs\":true,\"tr\":10} ,\n"
                        + " {\"seq\":\"2/2\",\"contactList\":[{\"ringID\":2110074941,\"uId\":\"2110074941\",\"fn\":\"FINAL RELEASE\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"\",\"prImId\":0,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":56448,\"ut\":1460958817088,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":1,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110010031,\"uId\":\"2110010031\",\"fn\":\"Jhon Snow\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"cloud/uploaded-136/2110010031/3494261453710895305.jpg\",\"prImId\":10449,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":25,\"ut\":1459254948848,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":1,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110010188,\"uId\":\"2110010188\",\"fn\":\"towhid\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"2110010188/1442394235711.jpg\",\"prImId\":369,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":448,\"ut\":1455193190172,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":0,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110010028,\"uId\":\"2110010028\",\"fn\":\"Alamgir Kabir\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"\",\"prImId\":0,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":18,\"ut\":1436424093413,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":0,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0},{\"ringID\":2110075137,\"uId\":\"2110075137\",\"fn\":\"naseeftest\",\"cnId\":0,\"bDay\":0,\"psnc\":0,\"dvc\":0,\"prIm\":\"\",\"prImId\":0,\"cImId\":0,\"prImPr\":1,\"isf\":false,\"utId\":56483,\"ut\":1455203044296,\"cut\":0,\"ists\":0,\"actv\":0,\"mtl\":false,\"nmf\":0,\"nvldt\":0,\"cimX\":0,\"cimY\":0,\"ispc\":0,\"isepc\":0,\"ismnv\":0,\"iev\":0,\"ianv\":0,\"wle\":0,\"ple\":0,\"itp\":0,\"ct\":0,\"frnS\":0,\"mDay\":0,\"bv\":0,\"rc\":0,\"del\":0,\"mb\":0,\"cft\":0,\"apt\":0,\"fda\":0,\"cla\":0,\"chta\":0,\"anc\":0,\"mfc\":0,\"maxID\":0}],\"sucs\":true,\"tr\":10}]";

                /* Type listType = new TypeToken<ArrayList<ContactListDTO>>() {
                }.getType();

                JsonParser parser = new JsonParser();
                JsonElement tradeElement = parser.parse(responseString);
                JsonArray trade = tradeElement.getAsJsonArray();
                List<ContactListDTO> contactLists = new Gson().fromJson(trade, listType);*/
                JsonReader responseReader = new JsonReader(new StringReader(responseString));
                responseReader.setLenient(true);

                ContactListDTO[] contactListDTOs = new Gson().fromJson(responseReader, ContactListDTO[].class);

                //ContactListDTO contactListDTO = gson.fromJson(responseString, ContactListDTO.class);
                int Type = 1;
                int Length = 1;

                int actionBytes = 4;

                int sessionIdBytes = requestObject.get("sId").getAsString().getBytes().length;

                int clientPacketIdBytes = requestObject.get("pckId").getAsString().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                totalHeaderSize = 4 * (Type + Length) + actionBytes + clientPacketIdBytes + 2 * sessionIdBytes;
                //  System.out.println("Total header size " + totalHeaderSize);

                byte[] header = new BrokenHeaderBuilder(totalHeaderSize)
                        .addString(Code.UNIQUE_KEY, requestObject.get("sId").getAsString())
                        .addInt(Code.ACTION, action, 4)
                        .addString(Code.CLIENT_PACKET_ID, requestObject.get("pckId").getAsString())
                        .addString(Code.SESSION_ID, requestObject.get("sId").getAsString())
                        .getBrokenHeader();

                printHeaderBytes(header);

                // List<UserDTO> contactList = contactListDTO.getContactList();
                listOfPackets = new ArrayList<>();
                ListByteBuilder listBuilder  = new ListByteBuilder();

                ByteBuilder byteBuilder = new ByteBuilder()
                        .setHeader(header);

                for (ContactListDTO contactListDTO : contactListDTOs) {

                    List<UserDTO> contactList = contactListDTO.getContactList();

                    for (UserDTO userDTO : contactList) {

                        if (userDTO.getGender() == null) {
                            userDTO.setGender(" ");
                        }
                        listBuilder = listBuilder
                                .addLong(Code.USER_IDENTITY, Long.parseLong(userDTO.getUserIdentity()), 8)
                                .addString(Code.USER_NAME, userDTO.getFullName())
                                .addString(Code.GENDER, userDTO.getGender())
                                .addInt(Code.COUNTRY_ID, userDTO.getCountryID(), 4)
                                .addLong(Code.BIRTH_DATE, userDTO.getBirthDate(), 8)
                                .addInt(Code.PRESENCE, userDTO.getPresence(), 4)
                                .addInt(Code.DEVICE, userDTO.getDevice(), 4)
                                .addString(Code.PROFILE_IMAGE, userDTO.getProfileImage())
                                .addLong(Code.PROFILE_IMAGE_ID, userDTO.getProfileImageId(), 8)
                                .addLong(Code.COVER_IMAGE_ID, userDTO.getCoverImageId(), 8)
                                .addInt(Code.PRIVACY, userDTO.getProfileImagePrivacy(), 4)
                                .addBool(Code.FRIENDSHIP_STATUS, userDTO.getIsFriend())
                                .addLong(Code.USER_ID, userDTO.getUserTableID(), 8)
                                .addLong(Code.UPDATE_TIME, userDTO.getUpdateTime(), 8)
                                .addLong(Code.CONTACT_UPDATE_TIME, userDTO.getContactUpdateTime(), 8)
                                .addInt(Code.STATUS, userDTO.getStatus(), 4)
                                .addInt(Code.IS_ACTIVE, userDTO.getIsActive(), 4)
                                .addBool(Code.IS_MUTUAL, userDTO.getMutual())
                                .addInt(Code.NO_MUTUAL_FRIENDS, userDTO.getNoOfMutualFriends(), 4)
                                .addInt(Code.NOTIFICATION_VALIDITY, userDTO.getNotificationValidity(), 4)
                                .addInt(Code.COVER_IMAGE_X, userDTO.getCoverImageX(), 4)
                                .addInt(Code.COVER_IMAGE_Y, userDTO.getCoverImageY(), 4)
                                .addInt(Code.IS_PICKED_EMAIL_FROM_PHONE, userDTO.getIsEmailPicked(), 4)
                                .addInt(Code.IS_PICKED_FROM_PHONE, userDTO.getIsNumberPicked(), 4)
                                .addInt(Code.IS_MY_NUMBER_VERIFIED, userDTO.getIsNumberVerified(), 4)
                                .addInt(Code.IS_EMAIL_VERIFIED, userDTO.getIsEmailVerified(), 4)
                                .addInt(Code.WEB_LOGIN_ENABLED, userDTO.getWebLoginEnabled(), 4)
                                .addInt(Code.PC_LOGIN_ENABLED, userDTO.getPcLoginEnabled(), 4)
                                .addInt(Code.INFORMATION_TYPE, userDTO.getInformationType(), 4)
                                .addInt(Code.CONTACT_TYPE, userDTO.getContactType(), 4)
                                .addInt(Code.FRIENDSHIP_STATUS, userDTO.getFriendShipStatus(), 4)
                                .addLong(Code.MARRIAGE_DAY, userDTO.getMarriageDay(), 8)
                                .addInt(Code.BLOCK_VALUE, userDTO.getBlockValue(), 4)
                                .addInt(Code.REASON_CODE, userDTO.getReasonCode(), 4)
                                .addInt(Code.MATCH_BY, userDTO.getMathedBy(), 4)
                                .addLong(Code.CALL_FORWARD_TO, userDTO.getCallForwardTo(), 8)
                                .addInt(Code.CALL_ACCESS, userDTO.getCallAccess(), 4)
                                .addInt(Code.CHAT_ACCESS, userDTO.getChatAccess(), 4)
                                .addInt(Code.FEED_ACCESS, userDTO.getFeedAccess(), 4)
                                .addInt(Code.APPLICATION_TYPE, userDTO.getAppType(), 4)
                                .addInt(Code.ANONYMOUS_CALL, userDTO.getAnonymousCall(), 4)
                                .addInt(Code.MUTUAL_FRIEND_COUNT, userDTO.getMutualFriendCount(), 4);

                    }

                    listBuilder = listBuilder
                            .addBool(Code.SUCCESS, contactListDTO.isSuccess())
                            .addInt(Code.TOTAL_RECORDS, contactListDTO.getTotalRecord(), 4);
                    
                    byte[] bytes = listBuilder.getListBytes();
                    
                    
                    
                   /* System.out.println(bytes.length);
                    
                    for (byte aByte : bytes) {
                        System.out.print(aByte +" ");
                    }
                    System.out.println("*****");*/
                    
                    byteBuilder = byteBuilder
                            .addBrokenBytes(Code.CONTACT, bytes);
                }

                listOfPackets = byteBuilder.build();

                break;

            }

            case Code.ACTION_MOBILE_VERIFICATION: {
                
                String responseString ="{\"sucs\":true,\"mg\":\"Verification code has been sent to your phone no.\",\"rc\":0}";

                AuthParameters authParameters = gson.fromJson(this.request, AuthParameters.class);

                JsonObject jsonObject = gson.fromJson(this.request, JsonObject.class);

                UserAuthFeedBack userAuthFeedBack = new Gson().fromJson(responseString, UserAuthFeedBack.class);

                String authParamString = gson.toJson(authParameters);

                
                int Type = 2;
                int Length = 1;

                int actionBytes = 4;

                int clientPacketIdBytes = authParameters.getPacketId().getBytes().length;
                //  System.out.println("Client Packet Id Byte size " + clientPacketIdBytes);

                
                listOfPackets = null;

                //System.err.println(authParameters.getEmailVerificationCode());
                totalHeaderSize = 2 * (Type + Length) + actionBytes + clientPacketIdBytes;

                byte[] header = new HeaderBuilder(totalHeaderSize)
                        .addInt(Code.ACTION, authParameters.getAction(), 4)
                        .addString(Code.CLIENT_PACKET_ID, authParameters.getPacketId())
                        .getHeader();

                printHeaderBytes(header);

                authParameters.setVerificationCode(" "); ;

                System.out.println(userAuthFeedBack.getMessage());
                
                listOfPackets = listOfPackets = new ByteBuilder()
                                .setHeader(header)
                                .addBool(Code.SUCCESS, userAuthFeedBack.isSuccess())
                                .addString(Code.MESSAGE, userAuthFeedBack.getMessage())
                                .addString(Code.MY_NUMBER_VERIFICATION_CODE, authParameters.getVerificationCode())
                                .addInt(Code.REASON_CODE, userAuthFeedBack.getReasonCode(), 4)
                                .build();
                break;
            }

        }

        return listOfPackets;
    }

    public void setRequest(String s) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.request = s;
    }

    private void printHeaderBytes(byte[] header) {

        System.out.println("Header");
        for (byte b : header) {
            System.out.print(b + " ");
        }
        System.out.println("");
    }
}
