package kis.model;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.upload.services.UploadedFile;

import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.persistence.Entity;

import kis.util.BinaryStreamResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Created by
 * User: yury
 * at
 * Date: 10.05.2009
 * Time: 20:11:12
 *
 * Пока ничего делать не будем, потому что вроде как гемор. Ещё тип файла
 * помнить надо (ну это можно делать сразу после загрузки файла)
 */
@Entity(name="file")
public class AttachedFile extends AbstractEntity {
    private String type;

    private String fileName;

    @Lob
    private byte[] content;

    /**
     * пустой конструктор -- требование hibernate и вообще бинов
     */
    public AttachedFile() {
        
    }
    /**
     * Создаём ентити на основе залитого файла. Тип определяется
     * при помощи доп. свойств интерфейса UploadedFile
     * @param file залитый при помощи компонента tapestry файл
     */
    public AttachedFile(UploadedFile file) {
        type = file.getContentType();
        InputStream inputStream = file.getStream();
        content = new byte [(int) file.getSize()]; // до 4-х метров пока
        fileName = file.getFileName();
        int read = 0;
        int i = 0;
        try {
            while ( (read = inputStream.read()) != -1) {
                content[i++] = (byte) read;
            }
        } catch (IOException e) {
            e.printStackTrace();  
        }
    }

    /**
     * возвращает поток с типом файла
     * @return
     */
    @Transient
    public StreamResponse getStream() {
        StreamResponse sr = new BinaryStreamResponse(type, new ByteArrayInputStream(content));
        return sr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
