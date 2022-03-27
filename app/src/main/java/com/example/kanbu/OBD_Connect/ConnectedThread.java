package com.example.kanbu.OBD_Connect;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import retrofit2.http.PUT;

public class ConnectedThread extends Thread{
    // 차량 데이터 수집 반복문 boolean
    static boolean getDataCancel = true;
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    StringBuilder read_stringBuilders = new StringBuilder();
    // AT코드 순서
    public final String[] DefaultATCommandArray = new String[]{"ATZ","ATE0","ATD0","ATSP0","ATH1","ATM0","ATS0","ATAT1","ATST64"};
    // 차량 데이터 OBD2_PID
    String speed = "010D";

    public ConnectedThread(BluetoothSocket bluetoothSocket){
        this.bluetoothSocket = bluetoothSocket;
        InputStream inputStream_2 = null;
        OutputStream outputStream_2 = null;

        // 임시 객체를 사용하여 입력 및 출력 스트림을 가져옵니다.
        // 멤버 스트림은 최종입니다.
        try {
            inputStream_2 = bluetoothSocket.getInputStream();
            outputStream_2 = bluetoothSocket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        inputStream = inputStream_2;
        outputStream = outputStream_2;
    }

    public void run(){
        final int bytes = 1024; // read()에서 반환된 바이트
        final byte[] buffer = new byte[bytes]; // 스트림에 대한 버퍼 저장소
        byte temp = 0;
        //TODO AT 커맨드 세팅
        Set_ATCommand(buffer, bytes, temp);

    }

    // AT 커맨드 세팅
    public void Set_ATCommand(byte[] buffer, int bytes, byte temp){
        try {
            //TODO AT 커맨드 세팅
            for (int i=0; i<DefaultATCommandArray.length; i++){
                write(DefaultATCommandArray[i]+"\r"); // EML327 스캐너에 연결하기위해선 끝에 \r을 붙여주어야 한다. (엔터같은 개념)
                // 지울것!!
                Log.i("DefaultATCommandArray", DefaultATCommandArray[i]);
                if (inputStream != null){
                    // 지울것!!
                    Log.i("inputStream != null", "inputStream != null");
                    int read = inputStream.read(buffer, 0, bytes);
                    temp = (byte) read;

                    String strBuffer = new String(buffer, 0, temp,StandardCharsets.UTF_8);
                    // 지울것!!
                    Log.i("AT_strBuffer", strBuffer);
                    // read한 문자 값이 >라면 ECU에서 응답이 끝난것으로 판단하고 받은 문자 데이터를 해석한다.
                    if (strBuffer.equals(">")){
                        read_stringBuilders.append(strBuffer);
                        String result_read_stringBuilders = read_stringBuilders.toString();
                        // 지울것!!
                        Log.i("result_read_stringBuilders", result_read_stringBuilders);
                        read_stringBuilders.delete(0, read_stringBuilders.length());
                    }else { // read한 문자 값이 >이 없고 그냥 문자라면 ECU에서 응답이 끝나지 않은 것으로 판단하고 받은 문자 데이터를 계속 더한다.
                        if (strBuffer.contains(">")){
                            read_stringBuilders.delete(0, read_stringBuilders.length());
                        }else {
                            read_stringBuilders.append(strBuffer);
                            // 지울것!!
                            Log.i("read_stringBuilders", read_stringBuilders.toString());
                        }
                    }
                }
                //TODO AT 커맨드 끝나면 데이터 날림
                if (i >=DefaultATCommandArray.length-1) {
                    Log.i("read if", "read if");
                    read();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read(){

        final int bytes2 = 1024; // read()에서 반환된 바이트
        final byte[] buffer2 = new byte[bytes2]; // 스트림에 대한 버퍼 저장소
        byte temp2 = 0;

        //TODO 차량 데이터 수집
        try {
            // inputStream이 null이 아니라면 여기서부터 반복문 시작
            if (inputStream != null){

                write(speed + "\r"); // 처음에만 한번 날리고 돌아오는 데이터가 제대로 다 들어오면 그때 다음 데이터를 보낸다.

                while (getDataCancel){
                    int read = inputStream.read(buffer2, 0, bytes2);
                    temp2 = (byte) read;
                    String strBuffer = new String(buffer2, 0, temp2, StandardCharsets.UTF_8);
                    // 지울것!!
                    Log.i("strBuffer", strBuffer);
                    if (strBuffer.equals(">")){
                        read_stringBuilders.append(strBuffer);                                      // ">"만 왔다는 것은 ECU가 마지막 데이터를 보낸것이며 마지막 문자열을 더해주고 문자열로 만든다.
                        String result_read_stringBuilders = read_stringBuilders.toString();
                        // 지울것!!
                        Log.i("result_read_stringBuilders", result_read_stringBuilders);
                        read_stringBuilders.delete(0, read_stringBuilders.length());                // 데이터를 뽑았으니 문자열을 쌓아놓은 stringBuilder를 초기화 시킨다.
                        write(speed + "\r");
                    }else {
                        if (strBuffer.contains(">")){
                            String read_data_Builder_result = read_stringBuilders.toString();
                            // 지울것!!
                            Log.i("read_stringBuilders_delete", read_data_Builder_result);
                            write(speed + "\r");                                              // ">"가 포함 되어있다는 것은 데이터가 전부 들어왔다는 것이기에 다음 데이터 전송
                            read_stringBuilders.delete(0, read_stringBuilders.length());
                        }else {
                            // 지울것!!
                            Log.i("No_strBuffer.equals", read_stringBuilders.toString());
                            read_stringBuilders.append(strBuffer);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 원격 장치에 데이터를 보내기 위해 메인 액티비티에서 이것을 호출합니다 */
    public void write(String input){
        try {
            if (outputStream != null){
                outputStream.write(input.getBytes(StandardCharsets.UTF_8)); //입력된 문자열을 바이트로 변환 후 삽입
                outputStream.flush();
                Log.i("write_data", input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 연결을 종료하기 위해 메인 액티비티에서 이것을 호출 */
    public void cancel(){
        try {
            bluetoothSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
