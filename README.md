<img width="1024" height="241" alt="remora" src="https://github.com/user-attachments/assets/7a38185d-6ece-44c0-adf2-326a3e2f503b" />

# Remora

**o7studios helper plugin for Gradle projects**

- Adds BuildConstants, Lombok & MavenCentral-Publishing
- Example configuration [here](https://github.com/o7studios/agones-java-sdk/blob/main/build.gradle.kts)

## Development

Full development setup available as [Development Container](https://containers.dev/).
Please use it for being able to tell "It works on my machine".

**Docker is required to be installed on your machine!**

### IntelliJ IDEA

- Open IntelliJ (Welcome screen)
- Navigate to `Remote Development` - `Dev Containers`
- Press `New Dev Container`
- Select `From VCS Project`
- Select and connect with `Docker`
- Select `IntelliJ IDEA`
- Enter `Git Repository`: `https://github.com/o7studios/cheetah`
- Select `Detection for devcontainer.json file` `Automatic`
- Press `Build Container and Continue`

### Development Container Issues

If you encounter an issue with setting up a development container, please
try to rebuild it first before opening a GitHub Issue. It's not uncommon
that some issues may fix themselves after a fresh container rebuild.