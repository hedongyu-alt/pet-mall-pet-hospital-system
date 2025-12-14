/**
 * 前端验证码生成工具
 * 用于生成图形验证码并进行前端验证
 */

/**
 * 生成随机验证码文本
 * @param {number} length - 验证码长度,默认4位
 * @returns {string} 验证码文本
 */
export function generateCaptchaText(length = 4) {
  // 排除容易混淆的字符: 0/O, 1/I/l, 2/Z 等
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ3456789';
  let captchaText = '';
  
  for (let i = 0; i < length; i++) {
    const randomIndex = Math.floor(Math.random() * chars.length);
    captchaText += chars[randomIndex];
  }
  
  return captchaText;
}

/**
 * 在Canvas上绘制验证码图片
 * @param {string} text - 验证码文本
 * @param {number} width - 画布宽度
 * @param {number} height - 画布高度
 * @returns {string} base64格式的图片数据URL
 */
export function drawCaptcha(text, width = 120, height = 45) {
  // 创建canvas元素
  const canvas = document.createElement('canvas');
  canvas.width = width;
  canvas.height = height;
  const ctx = canvas.getContext('2d');
  
  // 绘制背景
  drawBackground(ctx, width, height);
  
  // 绘制干扰线
  drawInterferenceLines(ctx, width, height);
  
  // 绘制验证码文本
  drawText(ctx, text, width, height);
  
  // 绘制干扰点
  drawInterferencePoints(ctx, width, height);
  
  // 返回base64图片
  return canvas.toDataURL('image/png');
}

/**
 * 绘制背景
 */
function drawBackground(ctx, width, height) {
  // 渐变背景
  const gradient = ctx.createLinearGradient(0, 0, width, height);
  gradient.addColorStop(0, '#f0f4f8');
  gradient.addColorStop(0.5, '#e8f5e9');
  gradient.addColorStop(1, '#f1f8e9');
  
  ctx.fillStyle = gradient;
  ctx.fillRect(0, 0, width, height);
}

/**
 * 绘制干扰线
 */
function drawInterferenceLines(ctx, width, height) {
  const lineCount = 4; // 干扰线数量
  
  for (let i = 0; i < lineCount; i++) {
    ctx.strokeStyle = getRandomColor(100, 200);
    ctx.lineWidth = Math.random() * 2 + 0.5;
    ctx.beginPath();
    
    // 随机起点和终点
    const startX = Math.random() * width;
    const startY = Math.random() * height;
    const endX = Math.random() * width;
    const endY = Math.random() * height;
    
    ctx.moveTo(startX, startY);
    
    // 使用贝塞尔曲线使线条更自然
    const cp1X = Math.random() * width;
    const cp1Y = Math.random() * height;
    const cp2X = Math.random() * width;
    const cp2Y = Math.random() * height;
    
    ctx.bezierCurveTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY);
    ctx.stroke();
  }
}

/**
 * 绘制验证码文本
 */
function drawText(ctx, text, width, height) {
  const charWidth = width / (text.length + 1);
  
  for (let i = 0; i < text.length; i++) {
    // 随机字体大小
    const fontSize = Math.random() * 8 + 22;
    ctx.font = `bold ${fontSize}px Arial, sans-serif`;
    
    // 随机颜色(深色)
    ctx.fillStyle = getRandomColor(20, 100);
    
    // 随机旋转角度
    const angle = (Math.random() - 0.5) * 0.4; // -0.2 到 0.2 弧度
    
    // 字符位置
    const x = charWidth * (i + 1);
    const y = height / 2 + fontSize / 3;
    
    // 保存当前状态
    ctx.save();
    
    // 移动到字符位置并旋转
    ctx.translate(x, y);
    ctx.rotate(angle);
    
    // 绘制字符
    ctx.fillText(text[i], 0, 0);
    
    // 恢复状态
    ctx.restore();
  }
}

/**
 * 绘制干扰点
 */
function drawInterferencePoints(ctx, width, height) {
  const pointCount = 50; // 干扰点数量
  
  for (let i = 0; i < pointCount; i++) {
    ctx.fillStyle = getRandomColor(50, 200);
    const x = Math.random() * width;
    const y = Math.random() * height;
    const radius = Math.random() * 1.5 + 0.5;
    
    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.fill();
  }
}

/**
 * 生成随机颜色
 * @param {number} min - RGB最小值
 * @param {number} max - RGB最大值
 * @returns {string} RGB颜色字符串
 */
function getRandomColor(min = 0, max = 255) {
  const r = Math.floor(Math.random() * (max - min) + min);
  const g = Math.floor(Math.random() * (max - min) + min);
  const b = Math.floor(Math.random() * (max - min) + min);
  return `rgb(${r}, ${g}, ${b})`;
}

/**
 * 验证码验证(不区分大小写)
 * @param {string} input - 用户输入的验证码
 * @param {string} correct - 正确的验证码
 * @returns {boolean} 是否匹配
 */
export function verifyCaptcha(input, correct) {
  if (!input || !correct) {
    return false;
  }
  return input.toUpperCase() === correct.toUpperCase();
}

/**
 * 生成完整的验证码对象
 * @returns {Object} 包含验证码文本和图片的对象
 */
export function generateCaptcha() {
  const text = generateCaptchaText(4);
  const image = drawCaptcha(text);
  
  return {
    text,
    image,
    timestamp: Date.now()
  };
}

