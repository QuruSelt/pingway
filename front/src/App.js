import './App.css';
import {Fun} from "./components/Fun";

let darkTheme = true;
let mutesound = false;

function displayFileTree() {
  const fileTree = document.getElementById('fileTree');
  const gitOption = document.getElementById('gitOption');
  if (fileTree.style.display === 'none' || fileTree.style.display === '') {
    displayTabs(fileTree, gitOption);
  } else {
    displayTabs(null, gitOption, fileTree);
  }
}

function displayGitOption() {
  const fileTree = document.getElementById('fileTree');
  const gitOption = document.getElementById('gitOption');
  if (gitOption.style.display === 'none' || gitOption.style.display === '') {
    displayTabs(gitOption, fileTree);
  } else {
    displayTabs(null, gitOption, fileTree);
  }
}

function displayTabs(tabToShow, ...tabsToHide) {
  if (tabToShow !== null)
    tabToShow.style.display = 'block';
  tabsToHide.forEach(tab => {
    tab.style.display = 'none';
  });
}

function displaySettingsTab() {
  const settingsTab = document.getElementById('SettingsTab');
  if (settingsTab.style.display === 'none' || settingsTab.style.display === '') {
    settingsTab.style.display = 'block';
  } else {
    settingsTab.style.display = 'none';
  }
}

function changeTheme() {
  const button = document.getElementById("ThemeChanger");
  const root = document.querySelector(':root')
  if (darkTheme) {
    root.style.setProperty("--background-color", "#ffffff")
    root.style.setProperty("--items-color", "#65A6AF")
    root.style.setProperty("--hover-color", "#205E67")
    root.style.setProperty("--text-color", "#000000")
    document.getElementById("APPBODY").style.backgroundImage="url(backgrounds/light.png)";
    button.innerHTML = "Thème clair"
  } else {
    root.style.setProperty("--background-color", "#090E27")
    root.style.setProperty("--items-color", "#178035")
    root.style.setProperty("--hover-color", "#193120")
    root.style.setProperty("--text-color", "#ffffff")
    document.getElementById("APPBODY").style.backgroundImage="url(backgrounds/dark.png)";
    button.innerHTML = "Thème sombre"
  }

  darkTheme = !darkTheme;
}

function closeVideo() {
  const videoContainer = document.getElementById('VideoFun');
  const iframe = videoContainer.querySelector('iframe');
  if ( iframe ) {
    const iframeSrc = iframe.src;
    iframe.src = iframeSrc;
  }
  videoContainer.style.display = 'none';
  const mainAudio = document.getElementById("MainTheme");
  if (mutesound === false)
    mainAudio.play();
}

function changeAudio(event) {
  const mainAudio = document.getElementById("MainTheme");
  mainAudio.pause()
  mainAudio.src = "/audio/backgrounds/" + event.target.value;
  mainAudio.play();
  mutesound = false;
}

function changeVolume(event) {
  const mainAudio = document.getElementById("MainTheme");
  mainAudio.volume = event.target.value / 100;
}

function UpdateLines() {
  const lineNumbers = document.getElementById("lineNumbers");
  const codeArea = document.getElementById("codeArea");
  console.log("UpdateLines" + lineNumbers.innerHTML);
  const lines = codeArea.value.split('\n').length;
  lineNumbers.innerHTML = Array(lines)
      .fill(0)
      .map((_, i) => `<span>${i + 1}</span>`)
      .join('<br>');
}

function syncScroll() {
  const lineNumbers = document.getElementById("lineNumbers");
  const codeArea = document.getElementById("codeArea");
  console.log("syncscroll");
  lineNumbers.scrollTop = codeArea.scrollTop;
}

function makeVideoDraggable() {
  const element = document.getElementById("VideoFun");
  const handle = document.getElementById("handle");
  let offsetX = 0, offsetY = 0, mouseX = 0, mouseY = 0;

  handle.onmousedown = dragMouseDown;

  function dragMouseDown(e) {
      e.preventDefault();
      mouseX = e.clientX;
      mouseY = e.clientY;
      document.onmouseup = closeDragElement;
      document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
      e.preventDefault();
      offsetX = mouseX - e.clientX;
      offsetY = mouseY - e.clientY;
      mouseX = e.clientX;
      mouseY = e.clientY;
      element.style.top = (element.offsetTop - offsetY) + "px";
      element.style.left = (element.offsetLeft - offsetX) + "px";
  }

  function closeDragElement() {
      document.onmouseup = null;
      document.onmousemove = null;
  }
}

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <audio id="MainTheme" className="mainAudio" autoplay>
          <source src="audio/backgrounds/default.mp3"/>
        </audio>
      </header>
      <body id="APPBODY" className="App-body">
      <div className="container">
        <div className="sidebar">
          <button id="fileTreeButton" className="sidebar-button" onClick={displayFileTree}>
            <svg id="displayFolder" xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                 className="bi bi-folder-plus" viewBox="0 0 16 16">
              <path
                d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14H9v-1H2.826a1 1 0 0 1-.995-.91l-.637-7A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09L14.54 8h1.005l.256-2.819A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m5.672-1a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
              <path
                d="M13.5 9a.5.5 0 0 1 .5.5V11h1.5a.5.5 0 1 1 0 1H14v1.5a.5.5 0 1 1-1 0V12h-1.5a.5.5 0 0 1 0-1H13V9.5a.5.5 0 0 1 .5-.5"/>
            </svg>
          </button>
          <button id="gitButton" className="sidebar-button" onClick={displayGitOption}>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-git"
                 viewBox="0 0 16 16">
              <path
                d="M15.698 7.287 8.712.302a1.03 1.03 0 0 0-1.457 0l-1.45 1.45 1.84 1.84a1.223 1.223 0 0 1 1.55 1.56l1.773 1.774a1.224 1.224 0 0 1 1.267 2.025 1.226 1.226 0 0 1-2.002-1.334L8.58 5.963v4.353a1.226 1.226 0 1 1-1.008-.036V5.887a1.226 1.226 0 0 1-.666-1.608L5.093 2.465l-4.79 4.79a1.03 1.03 0 0 0 0 1.457l6.986 6.986a1.03 1.03 0 0 0 1.457 0l6.953-6.953a1.03 1.03 0 0 0 0-1.457"/>
            </svg>
          </button>
          <button id="settingsButton" className="sidebar-button" onClick={displaySettingsTab}>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-gear"
                 viewBox="0 0 16 16">
              <path
                d="M8 4.754a3.246 3.246 0 1 0 0 6.492 3.246 3.246 0 0 0 0-6.492M5.754 8a2.246 2.246 0 1 1 4.492 0 2.246 2.246 0 0 1-4.492 0"/>
              <path
                d="M9.796 1.343c-.527-1.79-3.065-1.79-3.592 0l-.094.319a.873.873 0 0 1-1.255.52l-.292-.16c-1.64-.892-3.433.902-2.54 2.541l.159.292a.873.873 0 0 1-.52 1.255l-.319.094c-1.79.527-1.79 3.065 0 3.592l.319.094a.873.873 0 0 1 .52 1.255l-.16.292c-.892 1.64.901 3.434 2.541 2.54l.292-.159a.873.873 0 0 1 1.255.52l.094.319c.527 1.79 3.065 1.79 3.592 0l.094-.319a.873.873 0 0 1 1.255-.52l.292.16c1.64.893 3.434-.902 2.54-2.541l-.159-.292a.873.873 0 0 1 .52-1.255l.319-.094c1.79-.527 1.79-3.065 0-3.592l-.319-.094a.873.873 0 0 1-.52-1.255l.16-.292c.893-1.64-.902-3.433-2.541-2.54l-.292.159a.873.873 0 0 1-1.255-.52zm-2.633.283c.246-.835 1.428-.835 1.674 0l.094.319a1.873 1.873 0 0 0 2.693 1.115l.291-.16c.764-.415 1.6.42 1.184 1.185l-.159.292a1.873 1.873 0 0 0 1.116 2.692l.318.094c.835.246.835 1.428 0 1.674l-.319.094a1.873 1.873 0 0 0-1.115 2.693l.16.291c.415.764-.42 1.6-1.185 1.184l-.291-.159a1.873 1.873 0 0 0-2.693 1.116l-.094.318c-.246.835-1.428.835-1.674 0l-.094-.319a1.873 1.873 0 0 0-2.692-1.115l-.292.16c-.764.415-1.6-.42-1.184-1.185l.159-.291A1.873 1.873 0 0 0 1.945 8.93l-.319-.094c-.835-.246-.835-1.428 0-1.674l.319-.094A1.873 1.873 0 0 0 3.06 4.377l-.16-.292c-.415-.764.42-1.6 1.185-1.184l.292.159a1.873 1.873 0 0 0 2.692-1.115z"/>
            </svg>
          </button>
        </div>
        <div className="bottombar">
          <button id="funButton" className="fun-button" onClick={Fun}>
          </button>
          <button className="bottombar-button">
            Console
          </button>
          <button className="bottombar-button">
            Run
          </button>
          <button className="bottombar-button">
            Debug
          </button>
          <button className="bottombar-button bottombar-display">
          </button>
        </div>
        <div id="fileTree" className="sideTab">
          File Tree
        </div>
        <div id="gitOption" className="sideTab">
          Git Options
        </div>
        <div id="textZone" className="textZone">
          <div className="line-numbers" id="lineNumbers"></div>
          <textarea id="codeArea" cols="5" spellcheck="false" onScroll={syncScroll} onInput={UpdateLines}>
          </textarea>
        </div>
      </div>
      <div id="VideoFun" className="videoContainer draggable resizable">
        <div className="frameOverlay"></div>
        <div id="handle" className="handle" onDragEnter={makeVideoDraggable}></div>
        <iframe id="videoPlayer" width="700" height="500" src="https://www.youtube.com/embed/GnLHMXHqZXo"></iframe>
        <button onClick={closeVideo}>Fermer Vidéo</button>
      </div>
      <div id="SettingsTab" className="settingsTab">
        <div align="right" id="closeSettingsButton">
          <button id="CloseSettings" onClick={displaySettingsTab}>X</button>
        </div>
        <br/>
        <div>
          Choisissez un thème visuel:
          <button id="ThemeChanger" onClick={changeTheme}>Thème sombre</button>
        </div>
        <br/>
        <div>
          <label htmlFor="ThemeChoice">Choisissez un thème sonore:  </label>
          <select name="themes" id="theme-select" onChange={changeAudio}>
            <option value="default.mp3">Vagues</option>
            <option value="storm1.mp3">Orage</option>
            <option value="lofi1.mp3">Oceanic Lo-Fi</option>
            <option value="500-milliseconds-of-silence.mp3">Aucun</option>
          </select>
        </div>
        <br/>
        <div>
          Volume du fond sonore:
          <input id="slider" type="range" min="0" max="100" defaultValue="100" onChange={changeVolume}/>
        </div>
      </div>
      </body>
    </div>
  )
    ;
}


export default App;
