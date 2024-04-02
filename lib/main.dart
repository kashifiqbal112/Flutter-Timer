import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:timer/helper/native_service_helper.dart';

Future<void> main() async {

  WidgetsFlutterBinding.ensureInitialized();
   await NativeServiceHelper.startForegroundService();
   await NativeServiceHelper.startTimerActivity();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {


  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('com.example.timer/Timer');
  String _timerValue = '0';
  int _counter = 0;

  @override
  void initState() {
    platform.setMethodCallHandler(_handleMethod);
    super.initState();
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "updateTimer":
        setState(() {
          _timerValue = call.arguments.toString();
        });
        break;
    }
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(

              '${_timerValue} seconds',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
