package union;

import haven.UI;
import haven.Coord;
import haven.Resource;
import haven.Tex;
import haven.TexI;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import union.CustomMenu.MenuElement;
import union.CustomMenu.MenuElemetUseListener;

public class JSScriptInfo {
	/* Static */
	protected static Resource defaultIcon = Resource.load("paginae/union/scripts/script3");
	public static HashMap<String, JSScriptInfo> script_list = new HashMap<String, JSScriptInfo>();
	public static HashMap<String, MenuElement> scriptFolders = new HashMap<String, MenuElement>();
	
	public static void LoadAllScripts() {
		Iterator<Map.Entry<String, JSScriptInfo>> i = script_list.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<String, JSScriptInfo> e = i.next();
			if (!new File(e.getKey()).exists()) {
				e.getValue().RemoveStartMenuElement();
				i.remove();
			}
		}

		Iterator<Map.Entry<String, MenuElement>> i1 = scriptFolders.entrySet().iterator();
		while (i1.hasNext()) {
			Map.Entry<String, MenuElement> e = i1.next();
			if (!new File(e.getKey()).exists()) {
				e.getValue().RemoveFromMenu();
				i1.remove();
			}
		}

		loadFolder(null, new File("scripts"));
	}

	public static void loadFolder(MenuElement prevFolder, File folder) {
		if (folder.exists() && folder.isDirectory()) {
			MenuElement curFolder = null;
			if (prevFolder != null) {
				curFolder = scriptFolders.get(folder.getPath());
				if (curFolder == null) {
					curFolder = APXUtils.addResource(folder.getPath(), folder.getName(), "", '\0', APXUtils.resScript6, prevFolder, null);
					scriptFolders.put(folder.getPath(), curFolder);
				}
			}
			File[] files = folder.listFiles(new FileFilter() {
				public boolean accept(File f) {
					return (f.isDirectory() || f.getName().endsWith(".jbot"));
				}
			});
			if (files != null) {
				for (File f : files) {
					if (f.isFile()) {
						LoadScriptFromFile(f, curFolder);
					} else if (f.isDirectory()) {
						loadFolder((curFolder == null ? APXUtils.scriptRootNew : curFolder), f);
					}
				}
			}
		}
	}
	
	public static boolean hasUniq(String uniq) {
		for (JSScriptInfo info : script_list.values()) {
			if (info.scrUniq != null && info.scrUniq.equals(uniq)) return true;
		}
		return false;
	}
	
	public static JSScriptInfo LoadScriptFromFile(File file, MenuElement folder) {
		String path = file.getPath();
		JSScriptInfo ret = script_list.get(path);
		if (ret != null) {
			if (ret.olderThen(file)) {
				ret.LoadFromFile(file);
			}
		} else {
			script_list.put(path, ret = new JSScriptInfo(file, folder));
		}

		return ret;
	}

	public static JSScriptInfo getScript(File file) {
		return script_list.get(file.getPath());
	}
	
	public static void LoadAllScriptsToMenu() {
		/*
		 * Folder menu elements have to be rebuilt here because addMenu() throws
		 * the whole tree away and creates a fresh unionRoot/scriptRootNew, which
		 * orphans everything currently held in scriptFolders.
		 *
		 * The previous version had three defects that together produced the
		 * "two icons per folder, then the menu freezes on the second open" bug:
		 *
		 *  1. It re-keyed scriptFolders by folder.uniq_id. That id already
		 *     carries CustomMenu.menu_prefix (MenuElement's constructor prepends
		 *     it), so passing it back into addResource() prefixed it a second
		 *     time. loadFolder() looks folders up by FILE PATH, so from then on
		 *     its scriptFolders.get(folder.getPath()) always missed and it built
		 *     a duplicate element - and MenuElement.addChild() appends with no
		 *     dedup check. One extra folder icon per open.
		 *  2. Nested folders were re-parented to folder.parent, a reference into
		 *     the discarded tree, because the guard compared uniq_id against the
		 *     bare literal "script_start" - which can never match a prefixed id.
		 *     HashMap iteration order also meant a child could be rebuilt before
		 *     its parent existed.
		 *  3. Nothing detached the old elements, so parent.children grew without
		 *     bound on every open until the menu hung.
		 *
		 * scriptFolders stays keyed by file path throughout, which is what both
		 * loadFolder() and LoadAllScripts() expect.
		 */

		// 1. Tear down the folder elements from the previous pass. RemoveFromMenu
		//    only drops the Resource from paginae, so detach from the parent too.
		for (MenuElement folder : scriptFolders.values()) {
			folder.RemoveFromMenu();
			if (folder.parent != null) folder.parent.children.remove(folder);
		}

		// 2. Rebuild shallowest path first, so a parent always exists before any
		//    of its children need it.
		java.util.List<String> paths = new java.util.ArrayList<String>(scriptFolders.keySet());
		java.util.Collections.sort(paths, new java.util.Comparator<String>() {
			public int compare(String a, String b) {
				int da = a.split("[\\\\/]").length;
				int db = b.split("[\\\\/]").length;
				return (da != db) ? (da - db) : a.compareTo(b);
			}
		});

		HashMap<String, MenuElement> temp = new HashMap<String, MenuElement>();
		for (String path : paths) {
			File dir = new File(path);
			MenuElement parent = temp.get(dir.getParent());
			if (parent == null) parent = APXUtils.scriptRootNew;
			temp.put(path, APXUtils.addResource(path, dir.getName(), "", '\0',
					APXUtils.resScript6, parent, null));
		}
		scriptFolders = temp;

		// 3. Rebind every script to the freshly created folder element. The old
		//    reference points into the discarded tree, and AddStartMenuElement()
		//    is a no-op while elementStart is set, so drop it first.
		for (JSScriptInfo info : script_list.values()) {
			if (!info.scrHide) {
				String parentPath = (info.filename == null) ? null : new File(info.filename).getParent();
				MenuElement f = (parentPath == null) ? null : scriptFolders.get(parentPath);
				if (f != null || info.folder != null) info.RemoveStartMenuElement();
				info.folder = f;
				info.AddStartMenuElement();
			}
		}
	}
	
	public static void RemoveAllScriptsFromMenu() {
		for (JSScriptInfo info : script_list.values()) {
			if (!info.scrHide) info.RemoveStartMenuElement();
		}

		for (MenuElement folder : scriptFolders.values()) {
			folder.RemoveFromMenu();
		}
	}
	/* End of static */
	
	public String scrName; // Название скрипта
	public String scrTooltip; // Тултип
	public String scrContent; // Содержимое скрипта
	public char scrHotkey; // Хоткей
	public Resource scrIcon; // Иконка в меню
	public String scrUniq; // Уникальный идентификатор скрипта
	public boolean scrHide; // dontshow

	public MenuElement folder; // папка, где лежит скрипт (null, если в корневом каталоге)

	// Массив всех токенов
	protected HashMap<String, String> tokens;
	// Время последнего изменения файла скрипта
	protected long lastModified = 0;
	// Имя файла
	protected String filename;
	// Элемент в меню запуска скрипта
	public MenuElement elementStart;
	// Элемент в меню остановки скрипта
	public MenuElement elementStop;
	// Поток
	public JSThread scrThread;

	public JSScriptInfo(File script, MenuElement folder) {
		LoadFromFile(script);
		this.folder = folder;
	}

	public void LoadFromFile(File script) {
		lastModified = script.lastModified();
		try {
			filename = script.getPath();
			//FileReader freader = new FileReader(script);
			//BufferedReader reader = new BufferedReader(freader);
			StringBuilder builder = new StringBuilder();
			tokens = new HashMap<String, String>();

			//String buffer;
			//while ((buffer = reader.readLine()) != null) {
			List<String> list;
			try {
				list = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			} catch (MalformedInputException mie) {
				list = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
			}
			for (String buffer : list) {
				if (buffer.startsWith("//#!")) {
					if (buffer.contains("=")) {
						String[] token_buffer = buffer.substring(4).split("=");
						if (token_buffer.length == 2)
							tokens.put(token_buffer[0].trim(),
									token_buffer[1].trim());
					} else {
						tokens.put(buffer.substring(4).trim(), "");
					}
				}
				builder.append(buffer);
				builder.append('\n');
			}
			// Name
			if (tokens.containsKey("name"))
				scrName = tokens.get("name");
			else
				scrName = script.getName();
			// Tooltip
			if (tokens.containsKey("tooltip"))
				scrTooltip = tokens.get("tooltip");
			else
				scrTooltip = "";
			// Hotkey
			if (tokens.containsKey("hotkey"))
				scrHotkey = tokens.get("hotkey").charAt(0);
			else
				scrHotkey = '\0';
			// Icon
			if (tokens.containsKey("icon")) {
				scrIcon = Resource.load(tokens.get("icon"));
				scrIcon.loadwait();
				Resource.Image zimg = scrIcon.layer(Resource.imgc);
				Resource nr = new Resource(tokens.get("icon")+"_");
				if (zimg != null) {
					Tex btex = zimg.newTex();
					if (btex instanceof TexI) {
						Coord tSize = btex.sz();
						if(tSize.x > 30 || tSize.y > 30) {
							BufferedImage bim = APXUtils.scaleImage(((TexI)btex).back, 30, 30, new Color(0,0,0,0));
							nr.addLayer(scrIcon.new Image(zimg, bim));
							scrIcon = nr;
							//scrIcon.removeLayer(zimg);
							//scrIcon.addLayer(scrIcon.new Image(zimg, bim));
						}
					}
				}
			} else
				scrIcon = defaultIcon;
			// dontshow
			if (tokens.containsKey("dontshow"))
				scrHide = true;
			else
				scrHide = false;
			// Uniq
			if (scrUniq == null) { // Не генерировать уник заново для
									// перезагрузки скрипта
				if (tokens.containsKey("uniq") && !hasUniq(tokens.get("uniq")))
					scrUniq = tokens.get("uniq");
				else
					scrUniq = java.util.UUID.randomUUID().toString();
			}
			// Content
			scrContent = builder.toString();
			//reader.close();
			//freader.close();
		} catch (Exception e) {
			JSBot.JSError(e);
		}
	}

	public void AddStartMenuElement() {
		if (elementStart != null)
			return;
		elementStart = APXUtils.addResource("start_" + scrUniq, scrName,
				scrTooltip, scrHotkey, scrIcon, (folder == null ? APXUtils.scriptRootNew : folder),
				new MenuElemetUseListener(new String(filename)) {
					@Override
					public void use(int button) {
						if (info instanceof String) {
							JSScriptInfo script = script_list.get(info);
							script.Run();
						}
					}
				});
	}

	public void AddStopMenuElement() {
		if (elementStop != null)
			return;
		elementStop = APXUtils.addResource("stop_" + scrUniq, scrName,
				scrTooltip, scrHotkey, APXUtils.resScript4, APXUtils.scriptRootRem,
				new MenuElemetUseListener(new String(filename)) {
					@Override
					public void use(int button) {
						if (info instanceof String) {
							JSScriptInfo script = script_list.get(info);
							script.Stop();
						}
					}
				});
	}

	public void RemoveStopMenuElement() {
		if (elementStop != null) {
			elementStop.RemoveFromMenu();
			elementStop = null;
		}
	}
	
	public void RemoveStartMenuElement() {
		if (elementStart != null) {
			elementStart.RemoveFromMenu();
			elementStart = null;
		}
	}

	public void Run() {
		// Рестарт при повторном вызове. Зарпет на две копии одного скрипта при
		// одновременном выполнии
		if (isScriptRunning()) {
			Stop();
		}
		Update();
		scrThread = new JSThread(this) {

			@Override
			public void OnStart() {
					super.OnStart();
					AddStopMenuElement();
			}

			@Override
			public void OnStop() {
					super.OnStop();
					RemoveStopMenuElement();
			}
			
		};
		scrThread.start();
	}
	
	public void Stop() {
		UI.instance.m_util.forceStop();
		if (scrThread != null) {
			scrThread.Stop();
			scrThread.interrupt();
		}
	}
	
	public boolean isScriptRunning() {
		return scrThread != null;
	}

	public boolean olderThen(File script) {
		return script.lastModified() > lastModified;
	}
	
	public void Update() {
		File scriptFile = new File(filename);
		if (scriptFile.exists()) {
			if (olderThen(scriptFile)) {
				LoadFromFile(scriptFile);
			}
		} else {
			RemoveStartMenuElement();
			script_list.remove(filename);
		}
	}
}