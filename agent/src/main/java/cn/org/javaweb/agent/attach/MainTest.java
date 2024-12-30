package cn.org.javaweb.agent.attach;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainTest {
    public static void main(String[] args) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入一个字符串：");
        String input = reader.readLine();
        say(input);
        reader.close();

    }
    public static void say(String str){
        System.out.println("will be validate");
    }

}
