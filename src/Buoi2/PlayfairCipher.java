/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Buoi2;

import java.util.HashSet;
import java.util.Set;

public class PlayfairCipher {

    private char[][] keyMatrix;
    private static final int MATRIX_SIZE = 5;

    public PlayfairCipher(String key) {
        keyMatrix = generateKeyMatrix(key);
    }

    private char[][] generateKeyMatrix(String key) {
        char[][] matrix = new char[MATRIX_SIZE][MATRIX_SIZE];
        Set<Character> used = new HashSet<>();
        StringBuilder keyBuilder = new StringBuilder();

        // 1. Xử lý khóa (key)
        for (char ch : key.toCharArray()) {
            if (Character.isLetter(ch)) {
                ch = Character.toUpperCase(ch);
                if (ch == 'J') {
                    ch = 'I';
                }
                
                if (!used.contains(ch)) {
                    keyBuilder.append(ch);
                    used.add(ch);
                }
            }
        }

        // 2. Thêm các ký tự còn lại ('A' đến 'Z' trừ 'J')
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch == 'J') {
                continue;
            }

            if (!used.contains(ch)) {
                keyBuilder.append(ch);
                used.add(ch);
            }
        }

        // 3. Đổ vào ma trận 5x5
        int index = 0;
        for (int row = 0; row < MATRIX_SIZE; row++) {
            for (int col = 0; col < MATRIX_SIZE; col++) {
                matrix[row][col] = keyBuilder.charAt(index++);
            }
        }

        return matrix;
    }


    private String preprocessText(String text, boolean encode) {
        // Chuẩn hóa: Viết hoa, loại bỏ ký tự không phải chữ cái, thay 'J' bằng 'I'
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder sb = new StringBuilder(text);
        
        // Chèn 'X' giữa các ký tự trùng lặp và phân nhóm 2
        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1)) {
                sb.insert(i + 1, 'X');
            }
        }
        
        // Thêm 'X' nếu độ dài lẻ
        if (sb.length() % 2 != 0) {
            sb.append('X');
        }
        
        return sb.toString();
    }
    
    private String processText(String text, boolean encrypt) {
        text = preprocessText(text, encrypt);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            
            // Tính toán dịch chuyển: 1 bước cho mã hóa, 4 bước (tức -1 mod 5) cho giải mã
            int shift = encrypt ? 1 : 4;

            if (posA[0] == posB[0]) { // Cùng hàng
                result.append(keyMatrix[posA[0]][(posA[1] + shift) % MATRIX_SIZE]);
                result.append(keyMatrix[posB[0]][(posB[1] + shift) % MATRIX_SIZE]);
            } else if (posA[1] == posB[1]) { // Cùng cột
                result.append(keyMatrix[(posA[0] + shift) % MATRIX_SIZE][posA[1]]);
                result.append(keyMatrix[(posB[0] + shift) % MATRIX_SIZE][posB[1]]);
            } else { // Hình chữ nhật
                result.append(keyMatrix[posA[0]][posB[1]]);
                result.append(keyMatrix[posB[0]][posA[1]]);
            }
        }

        return result.toString();
    }

    public String encrypt(String plaintext) {
        return processText(plaintext, true);
    }

    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        // Chuẩn hóa đầu vào (chỉ áp dụng cho văn bản mã hóa)
        ciphertext = ciphertext.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char firstChar = ciphertext.charAt(i);
            char secondChar = ciphertext.charAt(i + 1);

            int[] pos1 = findPosition(firstChar);
            int[] pos2 = findPosition(secondChar);
            
            // Dịch chuyển 4 bước (tương đương -1 mod 5) để giải mã
            int shift = 4;

            if (pos1[0] == pos2[0]) { // Cùng hàng
                plaintext.append(keyMatrix[pos1[0]][(pos1[1] + shift) % 5]);
                plaintext.append(keyMatrix[pos2[0]][(pos2[1] + shift) % 5]);
            } else if (pos1[1] == pos2[1]) { // Cùng cột
                plaintext.append(keyMatrix[(pos1[0] + shift) % 5][pos1[1]]);
                plaintext.append(keyMatrix[(pos2[0] + shift) % 5][pos2[1]]);
            } else { // Hình chữ nhật
                plaintext.append(keyMatrix[pos1[0]][pos2[1]]);
                plaintext.append(keyMatrix[pos2[0]][pos1[1]]);
            }
        }

        // Hậu xử lý: Loại bỏ các ký tự 'X' được chèn vào trước đó
        for (int i = 1; i < plaintext.length() - 1; i++) {
            if (plaintext.charAt(i) == 'X' &&
                plaintext.charAt(i - 1) == plaintext.charAt(i + 1)) {
                plaintext.deleteCharAt(i);
            }
        }

        // Loại bỏ 'X' cuối cùng nếu nó được chèn để làm chẵn độ dài
        if (plaintext.length() > 0 && plaintext.charAt(plaintext.length() - 1) == 'X') {
            plaintext.deleteCharAt(plaintext.length() - 1);
        }

        return plaintext.toString();
    }

    private int[] findPosition(char ch) {
        for (int row = 0; row < MATRIX_SIZE; row++) {
            for (int col = 0; col < MATRIX_SIZE; col++) {
                if (keyMatrix[row][col] == ch) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
    
    public String getKeyMatrixAsString(){
        StringBuilder matrixString = new StringBuilder();
        for (int row = 0; row < MATRIX_SIZE; row++){
            for ( int col = 0; col < MATRIX_SIZE; col++){
            matrixString.append(keyMatrix[row][col]).append(" ");
        }
        matrixString.append("\n");
    }
        return matrixString.toString();
    }
}
