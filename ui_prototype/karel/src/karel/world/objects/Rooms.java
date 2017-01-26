/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package karel.world.objects;

import java.util.HashMap;
import java.util.Map;

public class Rooms<K1, K2, V> {

    private HashMap<K1, HashMap<K2, V>> rooms;

    public Rooms() {
        rooms = new HashMap<>();
    }

    public boolean put(K1 row, K2 col, V room) {
        HashMap<K2, V> colMap;
        if (rooms.containsKey(row)) {
            colMap = rooms.get(row);
            if (colMap.containsKey(col)) {
                return false;
            }
            else {
                colMap.put(col, room);
            }
        }
        else {
            colMap = new HashMap<>();
            colMap.put(col, room);
            rooms.put(row, colMap);
        }
        return true;
    }

    public V get(K1 row, K2 col) {
        if (rooms.containsKey(row)) {
            Map<K2, V> yMap = rooms.get(row);
            if (yMap.containsKey(col)) {
                return yMap.get(col);
            }
        }
        return null;
    }
    
    public void reset() {
        rooms = new HashMap<>();
    }
}