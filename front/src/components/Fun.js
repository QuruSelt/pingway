const videos = ['TUS6KQOzRVA', 'bm0nLJuRNbw', 'DN59pKJoF34',
                          'j2H20eeeZ18', 'Y0bOPRSi-9A', 'CdlpJhHCFlc',
                          'GnLHMXHqZXo', 'iut_goWlVfw', 'U7gd6F4RYQg',
                          'vNvlZg_zh1s', 'HDypv-8jqYI', '1SG1sjDT58w', 'Bgnq4yhfje4', '9VqUwr376M4', 'm8jWrUNkybQ']

function getRandomInt(max) {
  return Math.floor(Math.random() * max);
}

export function Fun() {
  const one_in_a_hundred = getRandomInt(100)
  if (one_in_a_hundred <= 4) {
    const audio = new Audio("/audio/events/fun/nout.mp3");
    audio.play();
  } else {
    const audio = new Audio("/audio/events/fun/noot.mp3");
    audio.play();
  }

  const fifty_fifty = getRandomInt(1);
  if (fifty_fifty === 1) {
    // GIFS
  } else {
    // VIDEOS
  }

  const windowElement = document.getElementById('VideoFun');
  const mainAudio = document.getElementById("MainTheme");
  const iframe = windowElement.querySelector('iframe');
  if (!iframe.onplay) {
    const videoNumber = getRandomInt(videos.length - 1);
    const videoUrl = "https://www.youtube.com/embed/" + videos[videoNumber]
    iframe.src = videoUrl;
  }

  if (windowElement.style.display === 'none' || windowElement.style.display === '') {
    windowElement.style.display = 'block';
    mainAudio.pause();
  }
}