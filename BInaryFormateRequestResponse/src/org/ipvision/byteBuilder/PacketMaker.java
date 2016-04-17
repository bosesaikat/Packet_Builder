package org.ipvision.byteBuilder;

import org.ipvision.attribute.code.Code;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.ringid.feedbacks.ContactFeedBack;
import org.ringid.feedbacks.FeedBack;
import org.ringid.feedbacks.UserAuthFeedBack;
import org.ringid.feedbacks.UserDetailsFeedBack;
import org.ringid.receiverparams.AuthParameters;
import org.ringid.receiverparams.CallParameters;
import org.ringid.receiverparams.RequestParameters;
import org.ringid.users.UserDTO;

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

                String responseString = "{\"sucs\":true,\"userDetails\":{\"uId\":\"2110033856\",\"fn\":\"Bptpzsbcub\",\"prIm\":\" \",\"cIm\":\"\",\"prImId\":0,\"prImPr\":1,\"utId\":22061,\"ispc\":0,\"isepc\":0,\"frnS\":0,\"fda\":0,\"cla\":0,\"chta\":0},\"utId\":22061}";
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
                        .addByte(Code.USER_FOUND, userDTObytes)
                        .addLong(Code.USER_ID, userDTO.getUserTableID(), 8)
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
