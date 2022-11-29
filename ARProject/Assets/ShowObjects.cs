using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;


public class ShowObjects : MonoBehaviour
{
	public GameObject[] objects;
	public GameObject[] ui;
	public GameObject target;
	public InputField name;
	public Slider SliderSize;
	public Slider SliderV;
	public Slider SliderH;
	public Slider SliderZ;
	public GameObject camera;
	bool flag = false;

	void Start()
	{
		name.text = "gear";
		string str = UniClipboard.GetText();
		if (str.Contains("com.example.ARViewer/"))
		{
			name.text = str.Remove(0, "com.example.ARViewer/".Length);
		}
	}

	public void Show()
	{
		string str = name.text;
		if (str.Contains("com.example.ARViewer/"))
		{
			str = str.Remove(0, "com.example.ARViewer/".Length);
		}
		foreach (GameObject go in objects)
		{
			if (go.name == str)
			{
				flag = true;
				Variables.name = str;
				try {
					if (Variables.obj != null)
						Destroy(Variables.obj);
				} catch { }
				Variables.obj = Instantiate(go);
				Variables.obj.transform.SetParent(target.transform);
				Variables.obj.transform.position = target.transform.position;
				Variables.obj.transform.localScale = new Vector3(0.001f, 0.001f, 0.001f);
				Variables.obj.transform.position = new Vector3(0.0f, 0.1f, 0.0f);
				Variables.obj.SetActive(false);

				GameObject goscen = GameObject.Find("UserDefinedTarget-" + Variables.ind);
				if (goscen != null)
				{
					try {
						if (goscen.transform.GetChild(0).gameObject != null)
							Destroy(goscen.transform.GetChild(0).gameObject);
					} catch { }
					Variables.objscen = Instantiate(go);
					Variables.objscen.transform.SetParent(goscen.transform);
					Variables.objscen.transform.position = goscen.transform.position;
					Variables.objscen.transform.localScale = new Vector3(0.001f, 0.001f, 0.001f);
					Variables.objscen.transform.position = new Vector3(0.0f, 0.1f, 0.0f);
					camera.transform.rotation = Quaternion.Euler(270f, 0f, 0f);
				}

				SliderSize.value = 0.001f;
				SliderV.value = 0.0f;
				SliderH.value = 0.0f;
				SliderZ.value = 0.0f;
			}
		}
		if (!flag)
		{
			name.text = "Объект не найден";
		} else
		{
			foreach (GameObject go in ui)
			{
				go.SetActive(true);
				flag = false;
			}
		}
	}
}
