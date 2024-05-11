package me.rhys;

import lombok.Getter;
import me.rhys.config.ConfigLoader;
import me.rhys.config.ConfigValues;
import me.rhys.listener.PlayerListener;
import me.rhys.util.AccountObject;
import me.rhys.util.HTTPUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;

    private final List<AccountObject> accountObjects = new ArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ConfigValues configValues = new ConfigValues();

    @Override
    public void onEnable() {
        instance = this;
        new ConfigLoader().load();
        this.executorService.scheduleAtFixedRate(this::loadAccounts, 0L, 1L, TimeUnit.HOURS);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        this.executorService.shutdownNow();
    }

    private void loadAccounts() {
        this.accountObjects.clear();

        try {
            String response = HTTPUtil.getResponse("https://pastebin.com/raw/sXADz3nq");
            JSONArray jsonArray = new JSONArray(response);

            jsonArray.forEach(object -> {
                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;

                    if (jsonObject.has("uuid") && jsonObject.has("name")) {
                        this.accountObjects.add(
                                new AccountObject(jsonObject.getString("name"),
                                        UUID.fromString(jsonObject.getString("uuid")))
                        );
                    }
                }
            });

            getLogger().info("Loaded " + this.accountObjects.size() + " "
                    + (this.accountObjects.size() != 1 ? "accounts" : "account"));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean isLoginBad(UUID uuid) {
        return this.accountObjects.stream().anyMatch(accountObject -> accountObject.getUuid().compareTo(uuid) == 0);
    }
}
