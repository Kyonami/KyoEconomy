package org.kyonami.kyoeconomy.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;

public class Config {
    private String path;
    private File file;
    private FileConfiguration config;

    public Config(String path) {
        this.path = path;
        this.file = new File(path.replace("/", File.separator));
        this.config = YamlConfiguration.loadConfiguration(this.file);

        // 아예 새 빈문서 생성됨
        if(!this.file.exists()) {
            // 파일 만들기
            save();
        }
    }

    public Config(String path, InputStream resourceFile) {
        this.path = path;
        this.file = new File(path.replace("/", File.separator));
        this.config = YamlConfiguration.loadConfiguration(this.file);

        // 아예 새 리소스 생성됨
        if(!this.file.exists()) {
            makeResource(resourceFile);
            save();
        }
    }

    public void makeResource(InputStream resourceFile){
        if(this.file.exists()) {
            return;
        }
        this.file.getParentFile().mkdirs();
        copyFile(resourceFile);
    }

    private void copyFile(InputStream in){
        try {
            OutputStream out = new FileOutputStream(this.file);
            byte[] buffer = new byte[1024];
            int len;
            while((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try{
            this.config.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() { this.config = YamlConfiguration.loadConfiguration((file)); }

    public Boolean isSet(String path) {
        return this.config.isSet(path);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public Object get(String path, Object def) {
        return this.config.get(path, def);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public String getString(String path, String def) {
        return this.config.getString(path, def);
    }

    public List<Character> getCharList(String path) {
        return this.config.getCharacterList(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }

    public long getLong(String path) {
        return this.config.getLong(path);
    }

    public long getLong(String path, long def) {
        return this.config.getLong(path, def);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return this.config.getBoolean(path, def);
    }

    public List<?> getList(String path) {
        return this.config.getList(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public List<Long> getLongList(String path) {
        return this.config.getLongList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return this.config.getIntegerList(path);
    }

    public List<Double> getDoubleList(String path) {
        return this.config.getDoubleList(path);
    }

    public ItemStack getItemStack(String path) {
        return this.config.getItemStack(path);
    }

    @SuppressWarnings("unchecked")
    public List<ItemStack> getItemStackList(String path) {
        return (List<ItemStack>) this.config.getList(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
    }
}
