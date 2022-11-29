using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class HideUI : MonoBehaviour
{
	public GameObject[] objects;

	public void Close()
    {
		if (Variables.showMenu)
		{
			foreach (GameObject go in objects)
			{
				go.SetActive(true);
			}
			Variables.showMenu = false;
		} else
		{
			foreach (GameObject go in objects)
			{
				go.SetActive(false);
			}
			Variables.showMenu = true;
		}
	}
}
