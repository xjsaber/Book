function () {
  //�����Ƕ���һ�����������������⿪�ż���ӿ�
    oController = {
      addNumber : addNumber,
      addOpen : addOpen
  }

  //�����Ƿ��Ŷ�Ӧ�ļ������������ñհ��Ķ��ձ�������
  var opMap = {
      "+": function (a, b) { return b + a}, //����ӷ�����ıհ�
      "-": function (a, b) { return b - a}, //�����������ıհ�
      "*": function (a, b) { return b * a}, //����˷�����ıհ�
      "/": function (a, b) { return b / a}, //�����������ıհ�
      "=": function (a, b) { return a}     //�������ս��
  }

  //����������ֵ�������������뻺������ݽṹ
  var oMemery = {
      numStack : [],                        //������ֵ
      operStact : [],                       //�����ַ���
      inBuffer : ""                         //������ʾ����
  }

  function init()                           //��ʼ��
  {
    with(oMemery)
    {
        numStack.length = 0;                //�����ֵ��ջ
        operStact.length = 0;               //��ղ�������ջ
        numStack.push(0);                   //����ֵ��ջ������һ��0��Ϊջ��
        inBuffer = "";                      //������뻺��
        return inBuffer;                    //����պ�Ļ���ֵ��ʵ�����ǿ��ַ���''������ 
    }
  }

  function doOper()                         //������ʽ
  {
    with(oMemery)
    {
      if(operStact.length)                  //��������ջ����ֵ
      {
        try
        {
            //ȡ��ջ���������Ӧ�Ĳ�������
            var op = opMap[operStact.pop()];
            var args = [];

            //�÷�����Ҫ�ṩ����������������ͨ��op.length�õ�
            for(var i = 0; i < op.length; i++)
            {
              //����ֵ��ջ������ȡ��Ӧ�Ĳ��������д���
              args.push(numStack.pop());
            }
        }
      catch(ex)
      {
        alert(ex.message);
      }
      }
      return numStackp[numStack.length - 1];
    }
  }

  //��ʽ����ʾ����ֵ����ҪΪ�˷��ϼ�������ϰ�ߣ�����0��ʾ��0. (��С����)
  function formatBuff(buf) {
      if (buf == "")
          return "0.";
      else {
          buf = parseFloat(buf);
          return /\./.test(buf) ? buf : buf + "."; //������
      }
  }

  function addNumber(tok)   //������ֵ
  {
      with (oMemery) {
        try
        {
          var token;
          if(tok = "\b" )   //����������һ���˸�
              token = inBuffer.slice(0,-1)  //�ѻ����е�����ȥ��һ��
          else
              token = inBuffer + tok.toString()  //������������������
          //�����ֵ�ĵ�һλ��С���㣬��ʾ�ϲ���һ��0
          if(/^([\d]+(\.)?[\d]*)?$/.test(token))
          {
            inBuffer = token;  //������㣬��ȷ�Ͻ��ܣ�д�뻺��
          }   
        }
      }
  }
}