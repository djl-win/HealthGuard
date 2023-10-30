# Health Guard

_"Health Guard" is a comprehensive Android application dedicated to personal health management. This all-in-one app assists users in maintaining a healthy lifestyle, offering the ability to record health statistics, upload medical reports, and set medication reminders. It provides a visual representation of health data for an intuitive grasp of one's well-being and features real-time chat for sharing health insights with friends or family. Should there be any anomalies in a user's health data or a new medical report, the system promptly alerts the designated emergency contacts. "Health Guard" — protecting you and your loved ones' health._

_"Health Guard" 是一款综合性的 Android 应用程序，专注于个人健康管理。此应用集多种功能于一身，全方位帮助用户维护健康的生活方式。用户不仅可以跟踪个人健康数据、上传医疗报告，还可以设置用药提醒。应用还提供了健康数据的可视化展示，让用户能够直观地了解自己的健康状态。此外，它还包括实时聊天功能，让用户与朋友或家庭成员分享健康信息。当用户的健康数据出现异常或新增医疗报告时，系统会自动通知用户的紧急联系人。"Health Guard" —— 守护您及您家人的健康。_

## Table of Contents / 目录

- [Configuration / 配置](#configuration--配置)
- [Technologies / 技术栈](#technologies--技术栈)
- [Deployment / 部署](#deployment--部署)
- [Functions / 功能](#functions--功能)
- [Data Communication / 数据通信](#data-communication--数据通信)
- [Performance Optimization / 性能优化](#performance-optimization--性能优化)
- [Privacy / 隐私](#privacy--隐私)
- [License / 证书](#License--证书)

## Configuration / 配置

This project is an Android application with detailed system and project configurations. Additionally, the development and build environment configurations for this application are as follows / 此项目是一个 Android 应用程序，具有详细的系统和项目配置。此外，此应用程序的开发和构建环境配置如下：

- **Compile SDK Version / 编译 SDK 版本**: 33
- **Min SDK Version / 最小 SDK 版本**: 26
- **Target SDK Version / 目标 SDK 版本**: 33
- **Version Code / 版本代码**: 1
- **Version Name / 版本名称**: "1.0"
- **Java Version / Java 版本**: JDK 1.8
- **Gradle Version / Gradle 版本**: 8.0
- **Android Gradle Plugin Version / Android Gradle 插件版本**: 8.1
- **Google Services Plugin / Google 服务插件**: 'com.google.gms.google-services'

These configurations ensure the application runs properly on different devices and leverages the latest Android features and security enhancements. Please ensure your development environment meets these configuration requirements / 这些配置确保应用程序能在不同设备上正常运行，并利用了最新的 Android 功能和安全性能。请确保您的开发环境满足上述配置要求。


## Technologies / 技术栈

This project leverages a variety of modern technologies to provide robust functionality and a seamless user experience / 本项目利用各种现代技术提供强大的功能和无缝的用户体验。

- **Firebase / Firebase**: A comprehensive app development platform that delivers a host of features such as real-time database, user authentication, and cloud messaging / 一个全面的应用开发平台，提供实时数据库、用户认证和云消息等一系列功能。
  - **Firebase Auth / Firebase 认证**: Facilitates user authentication using various sign-in methods / 通过各种登录方法促进用户认证。
  - **Firebase Storage / Firebase 存储**: Provides secure file uploads and downloads for Firebase apps / 为 Firebase 应用程序提供安全的文件上传和下载。
  - **Firestore / Firestore**: A flexible, scalable database for mobile, web, and server development from Firebase / Firebase 提供的用于移动、网络和服务器开发的灵活、可扩展的数据库。
  - **Firebase FCM / Firebase FCM**: Firebase Cloud Messaging (FCM) is a cross-platform messaging solution that lets you reliably send messages at no cost / Firebase 云消息传递 (FCM) 是一种跨平台消息解决方案，可让您可靠地免费发送消息。
  - **Firebase Realtime Database / Firebase 实时数据库**: A cloud-hosted NoSQL database that lets you store and sync data between your users in real-time / 一个云托管的 NoSQL 数据库，让您可以实时存储和同步用户之间的数据。
- **Glide / Glide**: An efficient image loading library for Android focused on smooth scrolling / 一个高效的 Android 图像加载库，专注于平滑滚动。
- **Dicebear / Dicebear**: Provides unique, procedurally generated avatars based on user identifiers / 根据用户标识符提供独特的、程序生成的头像。
- **Material / Material**: Material Design is a design system – backed by open-source code – that helps teams build high-quality digital experiences / Material Design 是一个设计系统——由开源代码支持——帮助团队建立高质量的数字体验。
- **MPAndroidChart / MPAndroidChart**: A powerful Android chart library for creating complex graphs and charts / 一个强大的 Android 图表库，用于创建复杂的图形和图表。
- **Volley / Volley**: An HTTP library that makes networking for Android apps easier and faster / 一个 HTTP 库，使 Android 应用的网络更简单、更快。
- **Gson / Gson**: A Java serialization/deserialization library to convert Java Objects into JSON and back / 一个 Java 序列化/反序列化库，用于将 Java 对象转换成 JSON 以及反向转换。

These technologies have been chosen for their reliability, performance, and the vibrant communities supporting them. Each contributes to the app's capabilities in handling data, images, notifications, and user interactions in a secure and efficient manner / 这些技术因其可靠性、性能和支持它围广泛的社区而被选中。每一项技术都为应用程序在安全高效地处理数据、图片、通知和用户互动方面提供了支持。


## Deployment / 部署

To recompile and redeploy this application on a simulator or real device, you'll need to follow these specific steps in your development environment: / 要在模拟器或真实设备上重新编译和重新部署这个应用程序，您需要在开发环境中遵循以下特定步骤：

1. **Install & Configure Android Studio / 安装并配置 Android Studio**:
   - Download and install Android Studio from the [official website](https://developer.android.com/studio) if it's not already installed. / 如果尚未安装，请从[官方网站](https://developer.android.com/studio)下载并安装 Android Studio。
   - Launch Android Studio and import your project by selecting "Open an existing Android Studio project". / 启动 Android Studio，并通过选择“打开现有的 Android Studio 项目”来导入您的项目。

2. **Set Up JDK & Gradle / 设置 JDK 和 Gradle**:
   - Ensure JDK 1.8 is installed on your system. If not, download and install it from the [Oracle website](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html). / 确保您的系统上已安装 JDK 1.8。如果没有，请从 [Oracle 网站](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)下载并安装。
   - Confirm that your project is using Gradle 8.0 by checking the Gradle script in your project's root directory. (gradle-wrapper.properties) / 通过检查项目根目录中的 Gradle 脚本，确认您的项目正在使用 Gradle 8.0。（gradle-wrapper.properties）

3. **Update Project Dependencies & Sync Project / 更新项目依赖并同步项目**:
   - In Android Studio, open the `build.gradle` file and update any dependencies if necessary to ensure all are current. / 在 Android Studio 中，打开 `build.gradle` 文件，如有必要，请更新任何依赖项，以确保所有依赖项都是最新的。
   - Click on "Sync Project with Gradle Files" under the "File" menu to sync your project. / 点击“文件”菜单下的“与 Gradle 文件同步项目”以同步您的项目。

4. **Prepare Your Device / 准备您的设备**:
   - For real device: Enable "USB debugging" on your Android device and connect it to your computer via USB. / 对于真实设备：在您的 Android 设备上启用“USB 调试”，并通过 USB 将其连接到您的计算机。
   - For simulator: Ensure your AVD is correctly set up in Android Studio. / 对于模拟器：确保您的 AVD 在 Android Studio 中正确设置。

5. **Run the Application / 运行应用程序**:
   - In Android Studio, click on "Run" > "Run 'app'" and select your device or simulator as the target deployment option. / 在 Android Studio 中，点击“运行”>“运行'app'”，并选择您的设备或模拟器作为目标部署选项。

By following these steps, you'll be able to recompile and redeploy this application to a simulator or a real device for testing or production purposes. / 通过遵循这些步骤，您将能够将应用程序重新编译并重新部署到模拟器或真实设备上，以进行测试或生产目的。

## Functions / 功能

1. **User Registration / 用户注册**:
   - Users can register by creating an account through Firebase Auth. / 用户可以通过 Firebase Auth 注册，创建自己的账户。

2. **User Login / 用户登录**:
   - Authentication of users through Firebase Auth to access their accounts. / 通过 Firebase Auth 对用户进行身份验证，以访问他们的账户。

3. **Health Data Upload / 健康数据上传**:
   - Users can manually upload various health data such as diastolic pressure, heart rate, blood oxygen, etc., which will be stored in Firestore. / 用户可以手动上传各种健康数据，如舒张压、心率、血氧等，这些数据将存储在 Firestore 中。

4. **Medical Report Upload / 体检报告上传**:
   - Users can upload their medical reports as files to Firebase Storage. / 用户可以将他们的体检报告作为文件上传到 Firebase Storage。

5. **Medication Reminder Upload / 用药提醒上传**:
   - Users can record and upload their daily medication intake. / 用户可以记录他们每日的药物摄入并上传这些信息。

6. **Data Visualization / 数据可视化**:
   - Visualization tools for users to clearly see the frequency of their health data and medical report additions. / 为用户提供可视化工具，清晰地查看他们的健康数据和体检报告添加频率。

7. **Health History / 健康历史数据**:
   - Users can visualize their historical health data over time. / 用户可以随着时间的推移可视化地查看他们的历史健康数据。

8. **Detailed Health Data / 详细健康数据**:
   - Users can view detailed information of all their uploaded health data. / 用户可以查看他们所有上传的健康数据的详细信息。

9. **Detailed Medical Reports / 详细体检报告**:
   - Users can view detailed information for each uploaded medical report. / 用户可以查看每一份上传的体检报告的详细信息。

10. **Detailed Medication Reminders / 用药提醒详细信息**:
   - Users can view all their medication reminders in detail. / 用户可以详细地查看他们所有的用药提醒。

11. **Abnormal Health Data Alerts / 异常健康数据提醒**:
   - When users upload health data with abnormal values, the system will automatically alert all of the user's friends. / 当用户上传具有异常值的健康数据时，系统会自动提醒用户的所有好友。

12. **Medical Report Alerts / 体检报告提醒**:
   - When a new medical report is uploaded, the system will automatically notify all of the user's friends. / 当上传新的体检报告时，系统会自动通知用户的所有好友。

13. **Medication Alerts / 服药提醒**:
   - The system will automatically remind users to take their medication according to the schedule set by the user. / 系统将根据用户设置的时间表自动提醒用户按时服药。

14. **Add Friends / 添加好友**:
   - Users can add other users as friends by searching and adding. / 用户可以通过搜索和添加将其他用户添加为好友。

15. **Real-time Chat / 实时聊天**:
   - Users can chat in real time with their friends, sharing health information or providing support. / 用户可以与他们的好友进行实时聊天，分享健康信息或提供支持。

16. **Personalized Health Data Reference Values / 个性化健康数据参考值**:
   - Users can set or modify the reference values for their health data based on personal conditions for a more personalized health tracking experience. / 用户可以根据个人情况设置或修改健康数据的参考值，以获得更个性化的健康跟踪体验。


## Data Communication / 数据通信

In the application, a variety of technologies are employed to enable real-time communication and storage of data, ensuring instantaneous delivery and secure storage of information. Below are detailed descriptions of these technologies: / 在应用中，采用多种技术实现数据的实时通信和存储，确保信息的瞬时传递和安全存储。下面是对这些技术的详细描述：

- **Cellular and WiFi / 蜂窝和WiFi**: The application communicates with the Firebase cloud database for data transmission via cellular data and WiFi, ensuring timely and consistent data synchronization, regardless of the user's location. / 应用程序通过蜂窝数据和WiFi与Firebase云数据库通信进行数据传输，无论用户身处何地，都能确保数据同步的及时性和一致性。

- **Firebase / Firebase**: A comprehensive app development platform that delivers a host of features such as real-time database, user authentication, and cloud messaging. / 一个全面的应用开发平台，提供实时数据库、用户认证和云消息等一系列功能。
  - **Firebase Auth / Firebase 认证**: This is used for user sign-in and registration operations, providing a secure method of authentication. / 用于用户登录和注册操作，提供了一种安全的认证方法。
  - **Firestore / Firestore**: Firestore is used to store all data from project users. All stored data is encrypted to protect user privacy. / 用于存储项目用户的所有数据。所有存储的数据都进行了加密，以保护用户隐私。
  - **Firebase Realtime Database / Firebase 实时数据库**: This technology allows users to chat in real time over the network, ensuring instantaneous message delivery and synchronized updates. / 该技术允许用户通过网络实时聊天，确保消息的即时传递和同步更新。
  - **Firebase Storage / Firebase 存储**: Users' private files are securely stored in Firebase Storage, adding an extra layer of protection for data security. / 用户的私人文件被安全地存储在Firebase存储中，为数据安全提供了额外的保护层。
  - **Firebase FCM / Firebase FCM**: Users are notified in real time via Firebase Cloud Messaging (FCM) when their data shows abnormalities or when new medical reports are available. / 当用户数据出现异常或有新的医疗报告时，通过Firebase云消息传递(FCM)实时通知用户。

- **Dicebear / Dicebear**: The application retrieves user avatars from Dicebear via the internet, providing each user with a unique, programmatically generated avatar. / 应用程序通过互联网从Dicebear检索用户头像，为每个用户提供独特的、程序生成的头像。

- **Volley / Volley**: It sends HTTP requests over the network, specifically to the FCM database to increase notifications, ensuring users receive important information in real time. / 它通过网络发送HTTP请求，特别是到FCM数据库以增加通知，确保用户实时接收重要信息。

The integrated use of these technologies ensures the efficiency and security of the "Health Guard" application in terms of data communication, storage, and user notifications. / 这些技术的综合使用确保了"Health Guard"应用在数据通信、存储和用户通知方面的效率和安全性。

## Performance Optimization / 性能优化

The "Health Guard" application integrates a variety of technologies to optimize performance in terms of network bandwidth, computation resources, and device battery usage. / "Health Guard" 应用整合了多种技术，以优化网络带宽、计算资源和设备电池使用方面的性能。

### Network Bandwidth Usage / 网络带宽使用

- **Data Compression / 数据压缩**: Images are compressed by reducing the quality of JPEG files before upload, which saves bandwidth, particularly for user reports. / 在上传前，通过降低JPEG文件的质量来压缩图像，以节省带宽，特别是对于用户报告。
  
- **Image Loading Optimization / 图片加载优化**: The Glide image library is used for efficient loading and caching of avatar images, reducing redundant downloads through local caching. For example, after the user's avatar is loaded for the first time, it will be cached locally, and the second time it will not need to be downloaded. / 使用 Glide 图像库高效加载和缓存头像图像，通过本地缓存减少重复下载。比如，用户头像第一次加载之后，后面就会被本地缓存，第二次就不需要下载了。

- **Network Request Optimization / 网络请求优化**: Reduce redundant network requests. For example, viewing the same LiveData on different pages avoids redundant queries and reduces interaction with cloud databases. / 减少冗余的网络请求。例如，在不同的页面，观察同一份LiveData，避免多余的查询，减少和云数据库的交互。

- **Caching Strategy / 缓存策略**: Effective caching strategies are implemented to minimize repetitive downloads. For example, medical reports are cached locally once downloaded and only fetched again if there is an update. / 实施有效的缓存策略，以最小化重复下载。例如，一旦下载了医疗报告，就将其缓存在本地，仅在有更新时才重新获取。

### Computation Resource Usage / 计算资源使用

- **Cached User Information / 缓存用户信息**: User data, such as FCM messages token, is cached to reduce interactions with the database. / 用户数据，如FCM消息的Token，被缓存以减少与数据库的交互。

- **Optimized Data Retrieval / 优化的数据检索**: Queries to the database are minimized by strategies such as limiting the number of chat messages retrieved to a manageable amount, like 20. / 通过限制检索聊天消息的数量（例如20条）等策略，减少对数据库的查询。

- **Local Storage for User Preferences / 用户偏好的本地存储**: User preferences for medication reminders are stored locally and used to cancel reminders in alarms when the user logs out. / 用户的药物提醒偏好被本地存储，并在用户登出时用于取消警报中的提醒。

### Device Battery Usage / 设备电池使用

- **Battery-conscious Notifications / 考虑电池的通知**: Messages and notifications are only sent when the device battery level is above 50%, preserving battery life. / 只有当设备电池水平高于50%时，才发送消息和通知，以保护电池寿命。

- **Energy-efficient Features / 节能功能**: Features such as photo uploading and viewing are only available when the battery level is above 50%, preventing excessive battery drain. / 诸如照片上传和查看的功能只在电池电量高于50%时可用，防止过度耗电。


## Privacy / 隐私

### Data Security / 数据安全

- **Data Encryption / 数据加密**: "Health Guard" app utilizes the AES encryption algorithm (128-bit key, ECB mode, PKCS5Padding padding) to encrypt and decrypt all sensitive user information, securely storing it in the database. / "Health Guard" 应用采用 AES 加密算法（128 位密钥，ECB 模式，PKCS5Padding 填充）对所有用户敏感信息进行加密和解密，并将其安全存储在数据库中。

- **Password Hashing / 密码哈希**: User passwords undergo SHA-256 hash encryption to ensure password security. The hash values of passwords are stored in the database, not in plain text. / 用户密码经过 SHA-256 哈希加密，以确保密码的安全性。密码的哈希值存储在数据库中，而不是明文密码。

### Notification Privacy / 通知隐私

- **Notification Permissions / 通知权限**: We respect user privacy, and the "Health Guard" app requests notification permissions only with explicit user consent. This enables us to send important health reminders and notifications to users. / 我们尊重用户的隐私，"Health Guard" 应用仅在用户明确授权的情况下请求通知权限。这样，我们可以向用户发送重要的健康提醒和通知。

- **Alarm Permissions / 闹钟权限**: The app also requires alarm permissions to schedule medication reminders and other health-related tasks. These reminders are crucial for user health management, and we ensure that the use of these permissions is under user control and privacy protection. / 应用还需要闹钟权限，以便能够按计划提醒用户服药和执行其他健康操作。这些提醒对于用户的健康管理至关重要，我们将确保这些权限的使用是受到用户控制和隐私保护的。

We are committed to taking all necessary measures to protect user personal information and privacy, ensuring that the app's functionality operates under user authorization and privacy protection. / 我们致力于采取一切必要措施来保护用户的个人信息和隐私，并确保应用的功能在用户的授权和隐私保护下正常运行。


## License / 证书

This project is licensed under the GNU GPLv3 License - see the [LICENSE.md](https://github.com/djl-win/Healthguard/blob/master/LICENSE) file for details.


