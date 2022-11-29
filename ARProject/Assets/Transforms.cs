using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Transforms : MonoBehaviour
{
	public Slider SliderSize;
	public Slider SliderV;
	public Slider SliderH;
	public Slider SliderZ;

	public void TransformSize()
	{
			Variables.obj.transform.localScale = new Vector3(SliderSize.value, SliderSize.value, SliderSize.value);
		if (Variables.objscen != null)
			Variables.objscen.transform.localScale = new Vector3(SliderSize.value, SliderSize.value, SliderSize.value);
	}

	public void TransformRot()
	{
			int val = (int)SliderV.value;
			int val1 = (int)SliderH.value;
			int val2 = (int)SliderZ.value;
		Variables.obj.transform.rotation = Quaternion.Euler(val, val1, val2);
		if (Variables.objscen != null)
			Variables.objscen.transform.rotation = Quaternion.Euler(val, val1, val2);
	}
}
