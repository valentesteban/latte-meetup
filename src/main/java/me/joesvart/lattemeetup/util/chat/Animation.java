package me.joesvart.lattemeetup.util.chat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class Animation {

    @Getter
    private static Map<UUID, Map<String, Animation>> animations = Maps.newHashMap();

    private List<String> lines = Lists.newArrayList();
    private String name;
    private UUID uuid;
    private long seconds;
    private long current;
    private int index;
    private String lineCurrent;

    public Animation(String name, UUID uuid, int seconds){
        this.name = name;
        this.uuid = uuid;
        this.seconds = seconds * 20;

        Map<String, Animation> map = Maps.newHashMap();

        map.put(name, this);

        if(!animations.containsKey(uuid)){
            animations.put(uuid, map);
        }else{
            animations.get(uuid).put(name, this);
        }
    }

    public Animation(String name, UUID uuid, long seconds){
        this.name = name;
        this.uuid = uuid;
        this.seconds = seconds;

        Map<String, Animation> map = Maps.newHashMap();

        map.put(name, this);

        if(!animations.containsKey(uuid)){
            animations.put(uuid, map);
        }else{
            animations.get(uuid).put(name, this);
        }
    }

    public String getLine() {
        if (lineCurrent == null || lineCurrent.equals("")){
            return lineCurrent = lines.get(0);
        }
        if (current == seconds) {
            int newIndex = ++index;
            if(newIndex >= lines.size()){
                newIndex = 0;
                index = 0;
            }
            current = 0;
            lineCurrent = lines.get(newIndex);
        }
        current++;
        return lineCurrent;
    }

    public static Animation getAnimation(UUID uuid, String name){
        if (animations.get(uuid) == null){
            return null;
        }
        return animations.get(uuid).get(name);
    }
}
