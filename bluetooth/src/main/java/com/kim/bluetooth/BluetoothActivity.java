package com.kim.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private ListView bluetoothListView;
    private ListView bluetoothListView1;

    private BluetoothAdapter adapter;

    private List<String> list = new ArrayList<>();
    private BluetoothReceiver bluetoothReceiver;
    ArrayAdapter<String> arrayAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        adapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);

        bluetoothListView = (ListView) findViewById(R.id.bluetooth_list);
        bluetoothListView1 = (ListView) findViewById(R.id.bluetooth_list1);
        arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        bluetoothListView1.setAdapter(arrayAdapter1);
        showBluetooth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                showBluetooth();
                break;
            case R.id.menu_refresh1:
                showBluetooth1();
                break;
            case R.id.menu_could_show:
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                startActivity(discoverableIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showBluetooth() {
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            }
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            List<String> list;
            if (devices.size() > 0) {
                list = new ArrayList<>();
                for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                    BluetoothDevice device = (BluetoothDevice) iterator.next();
                    list.add(device.getName() + ":" + device.getAddress());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
                bluetoothListView.setAdapter(arrayAdapter);
            }
        } else {
            Toast.makeText(this, "无法检测到蓝牙设备", Toast.LENGTH_SHORT).show();
        }
    }

    public void showBluetooth1() {
        list.clear();
        adapter.startDiscovery();
    }

    private class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                list.add(device.getName() + ":" + device.getAddress());
                arrayAdapter1.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }
}
