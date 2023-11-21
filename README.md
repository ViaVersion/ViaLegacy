# ViaLegacy
ViaVersion addon to add support for EVERY Minecraft server version (Classic, Alpha, Beta, Release).

ViaLegacy is not usable by itself as a standalone software, as it is an addon for ViaVersion which adds more protocol translators.
ViaLegacy is intended to be implemented in a ViaVersion based protocol translator.

If you are looking to implement ViaLegacy in your own software you can start by reading the [Usage](#usage) section.  
If you just want to use ViaLegacy yourself you can check out some projects which implement it in the [Projects](#projects-implementing-vialegacy) section.

### Supported Server protocols
- Classic (c0.0.15 - c0.30 including [CPE](https://wiki.vg/Classic_Protocol_Extension))
- Alpha (a1.0.15 - a1.2.6)
- Beta (b1.0 - b1.8.1)
- Release (1.0.0 - 1.7.10)

The lowest supported client version from which ViaLegacy can translate is 1.7.2.

### Projects implementing ViaLegacy
 - [ViaProxy](https://github.com/ViaVersion/ViaProxy): Standalone proxy which uses ViaVersion to translate between Minecraft versions. Allows Minecraft 1.7+ clients to join to any version server.
 - [ViaFabricPlus](https://github.com/ViaVersion/ViaFabricPlus): Fabric mod for the latest Minecraft version with QoL fixes and enhancements to the gameplay.

## Releases
### Gradle/Maven
To use ViaLegacy with Gradle/Maven you can use the ViaVersion maven server:
```groovy
repositories {
    maven { url "https://repo.viaversion.com" }
}

dependencies {
    implementation("net.raphimc:ViaLegacy:x.x.x") // Get latest version from releases
}
```

```xml
<repositories>
    <repository>
        <id>viaversion</id>
        <url>https://repo.viaversion.com</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.raphimc</groupId>
        <artifactId>ViaLegacy</artifactId>
        <version>x.x.x</version> <!-- Get latest version from releases -->
    </dependency>
</dependencies>
```

### Jar File
If you just want the latest jar file you can download it from [GitHub Actions](https://github.com/RaphiMC/ViaLegacy/actions/workflows/build.yml) or the [ViaVersion Jenkins](https://ci.viaversion.com/view/All/job/ViaLegacy/).

## Usage
ViaLegacy requires you to have an already functional ViaVersion implementation for your platform.
If you don't have one you can check out [ViaLoader](https://github.com/ViaVersion/ViaLoader) for an abstracted and simplified, but still customizable implementation. 
You can also go to the other [ViaVersion](https://github.com/ViaVersion) repositories and look at their server and proxy implementations.

### Base Implementation
Note: In case you use [ViaLoader](https://github.com/ViaVersion/ViaLoader) you can skip "ViaLegacy platform implementation" and "Loading the platform" as ViaLoader already does that for you.

#### ViaLegacy platform implementation
To get started you should create a class which implements the ViaLegacy platform interface.
Here is an example:
```java
public class ViaLegacyPlatformImpl implements ViaLegacyPlatform {

    public ViaLegacyPlatformImpl() {
        this.init(this.getDataFolder());
    }

    @Override
    public Logger getLogger() {
        return Via.getPlatform().getLogger();
    }

    @Override
    public File getDataFolder() {
        return Via.getPlatform().getDataFolder();
    }

}
```
This is a very basic implementation which just uses the ViaVersion logger and data folder.

#### Loading the platform
After you have created your platform implementation you should load it in your ViaVersion implementation.
Here is an example:
```java
Via.getManager().addEnableListener(ViaLegacyPlatformImpl::new);
```
Make sure to add the enable listener before the Via manager is initialized (``((ViaManagerImpl) Via.getManager()).init();``).

It is also highly recommended to increase the max protocol path size of ViaVersion. The default value is 50 which means there can't be more than 50 protocols in one pipeline. This can get exceeded by ViaLegacy.
You can increase the path size by adding the following lines after the Via manager is initialized:
```java
Via.getManager().getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE); // Allow Integer.MAX_VALUE protocols in the pipeline
Via.getManager().getProtocolManager().setMaxPathDeltaIncrease(-1); // Allow unlimited protocol path size increase
((ProtocolManagerImpl) Via.getManager().getProtocolManager()).refreshVersions(); // Refresh the version paths
```
#### Implementing the netty changes
ViaLegacy requires you to make some changes to your netty pipeline where ViaVersion is being added into the pipeline.
ViaLegacy needs to have custom netty handlers in the pipeline which handle <= 1.6.4 connections. This is required due to the way how <= 1.6.4 (called pre-netty protocol) protocol differs.

To implement the changes you should add something similar to the following lines to your netty pipeline (After the ViaVersion handlers are added):
```java
if (serverTargetVersion.isOlderThanOrEqualTo(VersionEnum.r1_6_4)) { // Only add those handlers if the server version is <= 1.6.4
    user.getProtocolInfo().getPipeline().add(PreNettyBaseProtocol.INSTANCE); // Allow to intercept the handshake packet
    
    // You can either add a codec (if your pipeline is built for that)
    channel.pipeline().addBefore("length-codec", "vialegacy-pre-netty-length-codec", new PreNettyLengthCodec(user));
    // or two seperate netty handlers
    // channel.pipeline().addBefore("length-decoder", "vialegacy-pre-netty-length-prepender", new PreNettyLengthPrepender(user));
    // channel.pipeline().addBefore("length-encoder", "vialegacy-pre-netty-length-remover", new PreNettyLengthRemover(user));
}
```
In case you use [ViaLoader](https://github.com/ViaVersion/ViaLoader) and the [VLPipeline](https://github.com/ViaVersion/ViaLoader/blob/main/src/main/java/net/raphimc/vialoader/netty/VLPipeline.java) (or the [VLLegacyPipeline](https://github.com/ViaVersion/ViaLoader/blob/main/src/main/java/net/raphimc/vialoader/netty/VLLegacyPipeline.java)), you don't need to make these modifications anymore, as the VLPipeline/VLLegacyPipeline already does it automatically.
### Implementing the platform specific providers
The platform specific providers are all optional (except for ``EncryptionProvider`` and ``GameProfileFetcher``) and only required if you want to use the features which require them.  
To implement a provider you can simply call ``Via.getManager().getProviders().use(TheNameOfTheProvider.class, new YouImplementationOfThatProvider());`` after the Via manager is initialized.

#### EncryptionProvider
The encryption provider is used to encrypt the connection to the server if the server version is 1.2.5 - 1.6.4.  
Those servers enable the encryption at a different point in the login process than >= 1.7.2 servers. This means that your platform must not encrypt the connection when receiving the LoginKey packet from the server, but instead store the key and encrypt the connection when the method in the provider is invoked.  
If your platform supports online mode authentication you also should not always do a joinServer request when receiving the LoginKey packet, but only when ``userConnection.get(ProtocolMetadataStorage.class).authenticate`` is true.

#### GameProfileFetcher
The game profile fetcher is used to fetch the game profile of a player if the server version is <= 1.7.10.
This is required because the game profile or UUID of players is not sent by the server in <= 1.7.10.
The GameProfileFetcher is only used when ``legacy-skin-loading`` or ``legacy-skull-loading`` is set to ``true`` in the config. If you don't plan to use those options you don't need to implement this provider.
Here is an example implementation using the [Steveice10 AuthLib](https://github.com/GeyserMC/MCAuthLib):
```java
public class AuthLibGameProfileFetcher extends GameProfileFetcher {

    private static final SessionService sessionService = new SessionService();
    private static final ProfileService profileService = new ProfileService();

    @Override
    public UUID loadMojangUUID(String playerName) throws ExecutionException, InterruptedException {
        final CompletableFuture<com.github.steveice10.mc.auth.data.GameProfile> future = new CompletableFuture<>();
        profileService.findProfilesByName(new String[]{playerName}, new ProfileService.ProfileLookupCallback() {
            @Override
            public void onProfileLookupSucceeded(com.github.steveice10.mc.auth.data.GameProfile profile) {
                future.complete(profile);
            }

            @Override
            public void onProfileLookupFailed(com.github.steveice10.mc.auth.data.GameProfile profile, Exception e) {
                future.completeExceptionally(e);
            }
        });
        if (!future.isDone()) {
            future.completeExceptionally(new ProfileNotFoundException());
        }
        return future.get().getId();
    }

    @Override
    public GameProfile loadGameProfile(UUID uuid) throws ProfileException {
        final com.github.steveice10.mc.auth.data.GameProfile inProfile = new com.github.steveice10.mc.auth.data.GameProfile(uuid, null);
        final com.github.steveice10.mc.auth.data.GameProfile mojangProfile = sessionService.fillProfileProperties(inProfile);

        final GameProfile gameProfile = new GameProfile(mojangProfile.getName(), mojangProfile.getId());
        for (com.github.steveice10.mc.auth.data.GameProfile.Property prop : mojangProfile.getProperties()) {
            gameProfile.addProperty(new GameProfile.Property(prop.getName(), prop.getValue(), prop.getSignature()));
        }
        return gameProfile;
    }

}
```

#### OldAuthProvider
The old auth provider is used to authenticate the player if the server version is <= 1.2.5 and has online mode enabled.
This is required as <= 1.2.5 servers do not enable encryption at all and authenticate at a different point in the login process.  
The implementation of this provider requires the implementer to be able to send a joinServer request to the mojang servers using the supplied server hash.  
The default implementation of this provider simply throws an Exception indicating that online mode is not supported by this implementation.

#### AlphaInventoryProvider
The alpha inventory provider is used to get/set the inventory contents of the player if the server version is <= Alpha 1.2.6.
This is required as <= Alpha 1.2.6 servers handle the inventory fully clientside and the client is expected to send the whole inventory contents to the server when it changes anything.  
The default implementation of this provider uses an inventory tracking system which tracks the inventory contents of the player.

#### ClassicWorldHeightProvider
The classic world height provider is used to get the maximum world height the client is able to handle.
Classic server worlds can be up to 1024 blocks high, but <= 1.16.5 client can't handle worlds higher than 256 blocks.
The implementation of this provider requires the implementer to be able to determine the maximum supported world height of a given client.  
The default implementation of this provider states that the client supports 128 blocks high worlds.  

**Implementing this provider to have worlds higher than 256 blocks requires heavy modification of ViaVersion code which the implementer has to do on their own.**
For a reference implementation you can take a look at how [ViaProxy](https://github.com/ViaVersion/ViaProxy) implements it.

#### ClassicMPPassProvider
The classic MP pass provider is used to get the MP pass (Authentication token) of a player if the server version is <= Classic 0.30.
This is required for joining classic servers with enabled online mode.  
The implementation of this provider requires the implementer to be able to get the MP pass of a given player.  
The default implementation of this provider simply returns "0" which indicates to the server that the client does not have a valid MP pass.  
For a reference implementation you can take a look at how [ViaProxy](https://github.com/ViaVersion/ViaProxy/blob/main/src/main/java/net/raphimc/viaproxy/protocolhack/providers/ViaProxyClassicMPPassProvider.java) implements it.

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/ViaVersion/ViaLegacy/issues).  
If you just want to talk or need help implementing ViaLegacy feel free to join the ViaVersion
[Discord](https://discord.gg/viaversion).
