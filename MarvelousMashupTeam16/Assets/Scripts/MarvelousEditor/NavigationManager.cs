using UnityEngine;
using UnityEngine.SceneManagement;

namespace MarvelousEditor
{
    public class NavigationManager : MonoBehaviour
    {
        public enum Pages
        {
            CONFIG = 1, 
            MAP = 2, 
            CHARACTER = 3,
            EXIT = 4
        }
    
        private BackgroundHandler bh;
    
        [Header("Navigation Items:")]
        public RectTransform navigationConfig;
        public RectTransform navigationMap;
        public RectTransform navigationCharacter;
        public RectTransform navigationExit;

        [Header("Pages:")] 
        public RectTransform config;
        public RectTransform map;
        public RectTransform character;

        [Header("Defaults:")] 
        public Pages defaultPage;

        public int selectedTabHeight = 90;
        public int unselectedTabHeight = 60;
        [Range(0,0.1f)] public float fadeSpeed;
        private Pages showingPage;

        void Start()
        {
            showingPage = defaultPage;
            RectTransform rt = GetComponent<RectTransform>();
            rt.sizeDelta = new Vector2(rt.rect.width, selectedTabHeight);
            NavigationItemClicked((int) defaultPage);
        }
    
        public void NavigationItemClicked(int which)
        {
            navigationConfig.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
            navigationMap.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
            navigationCharacter.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
            navigationExit.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
        
            if (bh == null)
            {
                bh = BackgroundHandler.Get();
            }
        
            switch (which)
            {
                case (int) Pages.CONFIG:
                    navigationConfig.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.SELECTED);
                    showingPage = 0;
                    bh.MoveMap(bh.configOffset, () =>
                    {
                        config.gameObject.SetActive(true);
                        showingPage = Pages.CONFIG;
                    });
                    Debug.Log("Config tab selected!");
                    break;
                case (int) Pages.MAP:
                    navigationMap.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.SELECTED);
                    showingPage = 0;
                    bh.MoveMap(bh.mapOffset, () =>
                    {
                        map.gameObject.SetActive(true);
                        showingPage = Pages.MAP;
                    });
                    Debug.Log("Map tab selected!");
                    break;
                case (int) Pages.CHARACTER:
                    navigationCharacter.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.SELECTED);
                    showingPage = 0;
                    bh.MoveMap(bh.characterOffset, () =>
                    {
                        character.gameObject.SetActive(true);
                        showingPage = Pages.CHARACTER;
                    });
                    Debug.Log("Character tab selected!");
                    break;
                case (int) Pages.EXIT:
                    showingPage = 0;
                    SceneManager.LoadScene("MainMenu");
                    break;
            }
        }

        private void Update()
        {
            switch (showingPage)
            {
                case Pages.CONFIG:
                    config.GetComponent<CanvasGroup>().alpha += fadeSpeed;
                    character.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    map.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    break;
                case Pages.MAP:
                    config.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    character.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    map.GetComponent<CanvasGroup>().alpha += fadeSpeed;
                    break;
                case Pages.CHARACTER:
                    config.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    character.GetComponent<CanvasGroup>().alpha += fadeSpeed;
                    map.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    break;
                default:
                    config.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    character.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    map.GetComponent<CanvasGroup>().alpha -= fadeSpeed;
                    break;
            }
        
            if (config.GetComponent<CanvasGroup>().alpha == 0)
                config.gameObject.SetActive(false);
            if (map.GetComponent<CanvasGroup>().alpha == 0)
                map.gameObject.SetActive(false);
            if (character.GetComponent<CanvasGroup>().alpha == 0)
                character.gameObject.SetActive(false);
        }
    }
}
