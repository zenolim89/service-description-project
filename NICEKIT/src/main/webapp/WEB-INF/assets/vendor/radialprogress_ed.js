window.rp_requestAnimationFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.msRequestAnimationFrame || (function(callback, element) {
        setTimeout(callback, 1000 / 60);
    }
);

function RadialProgress(container, cfg) {
    container.innerHTML = "";
    
    var nc = document.createElement("div");
    nc.style.width = "70px";
    nc.style.height = "70px";
    nc.style.position = "relative";
    nc.className = "rp";
    
    container.appendChild(nc);
    container = nc;
    
    if (!cfg)
        cfg = {};
    
    this.progress = cfg.progress == undefined ? 0 : cfg.progress;

    this.colorBg = cfg.colorBg == undefined ? "#8ea9ec" : cfg.colorBg;
    this.colorFg = cfg.colorFg == undefined ? "#4270e0" : cfg.colorFg;
    this.innerBg = cfg.innerBg == undefined ? "transparent" : cfg.colorBg;
    this.innerFg = cfg.innerFg == undefined ? "#4270e0" : cfg.colorFg;

    this.colorText = cfg.colorText == undefined ? "#ffffff" : cfg.colorText;
    this.indeterminate = cfg.indeterminate == undefined ? false : cfg.indeterminate;
    this.round = cfg.round == undefined ? false : cfg.round;
    this.thick = cfg.thick == undefined ? 50 : cfg.thick;
    this.noAnimations = cfg.noAnimations == undefined ? true : cfg.noAnimations;
    this.fixedTextSize = cfg.fixedTextSize == undefined ? false : cfg.fixedTextSize;
    this.animationSpeed = cfg.animationSpeed == undefined ? 1 : cfg.animationSpeed > 0 ? cfg.animationSpeed : 1;
    this.noPercentage = cfg.noPercentage == undefined ? false : cfg.noPercentage;
    this.spin = cfg.spin == undefined ? false : cfg.spin;
    
    this.checkStauts = cfg.checkStauts == undefined ? true : cfg.checkStauts;

    this.endColorBg = cfg.endColorBg == undefined ? "#4270e0" : cfg.endColorBg;
    this.endInnerBg = cfg.endInnerBg == undefined ? "#4270e0" : cfg.endInnerBg;
    this.endColorText = cfg.endColorText == undefined ? "#ffffff" : cfg.endColorText;
    this.startColorText = cfg.startColorText == undefined ? "#999999" : cfg.startColorText;
    this.startColorBg = cfg.startColorBg == undefined ? "#e5e5e5" : cfg.startColorBg;

    if (cfg.noInitAnimation)
        this.aniP = this.progress;
    else
        this.aniP = 0;
    
    var c = document.createElement("canvas");
    c.style.position = "absolute";
    c.style.top = "0";
    c.style.left = "0";
    c.style.width = "100%";
    c.style.height = "100%";
    c.style.zIndex = 0;
    c.className = "rp_canvas";
    
    container.appendChild(c);
    this.canvas = c;

    var c2 = document.createElement("canvas");
    c2.style.position = "absolute";
    c2.style.top = "0";
    c2.style.left = "0";
    c2.style.width = "100%";
    c2.style.height = "100%";
    c2.className = "rp_canvas2";
    
    container.appendChild(c2);
    this.canvas2 = c2;

    var tcc = document.createElement("div");
    tcc.style.position = "absolute";
    tcc.style.display = "table";
    tcc.style.width = "100%";
    tcc.style.height = "100%";
    tcc.style.zIndex = 3;
    
    var tc = document.createElement("div");
    tc.style.display = "table-cell";
    tc.style.verticalAlign = "middle";
    
    var t = document.createElement("div");
    t.style.color = this.colorText;
    t.style.textAlign = "center";
    t.style.overflow = "visible";
    t.style.whiteSpace = "nowrap";
    t.className = "rp_text";

    tc.appendChild(t);
    tcc.appendChild(tc);
    container.appendChild(tcc);
    
    this.text = t;

    this.prevW = 0;
    this.prevH = 0;
    this.prevP = 0;
    this.indetA = 0;
    this.indetB = 0.2;
    this.rot = 0;

    this.draw = function(f, val) {
        if (!(f == true)) {
            // rp_requestAnimationFrame(this.draw);
        }

        if(val != null) {
            this.progress = val;
        }

        var c = this.canvas;
        var c2 = this.canvas2;
        var dp = window.devicePixelRatio || 1;

        c.width = c.clientWidth * dp;
        c.height = c.clientHeight * dp;
        
        c2.width = c2.clientWidth * dp;
        c2.height = c2.clientHeight * dp;

        if (!(f == true) && !this.spin && !this.indeterminate && (Math.abs(this.prevP - this.progress) < 1 && this.prevW == c.width && this.prevH == c.height))
            return;
        

        var centerX = c.width / 2
          , centerY = c.height / 2
          , bw = (c.clientWidth / 100.0)
          , radius = c.height / 2 - (this.thick * bw * dp) / 2
          , radius2  = c.height / 2 - (50 * bw * dp) / 2
          , bw = (c.clientWidth / 100.0);
        
        this.text.style.fontSize = (this.fixedTextSize ? (c.clientWidth * this.fixedTextSize) : (c.clientWidth * 0.26 - this.thick)) + "px";
        
        if (this.noAnimations)
            this.aniP = this.progress;
        else {
            var aniF = Math.pow(0.93, this.animationSpeed);
            this.aniP = this.aniP * aniF + this.progress * (1 - aniF);
        }
        

        c = c.getContext("2d");
        c.beginPath();
        c.strokeStyle = (this.progress == 0) ? this.startColorBg : this.colorBg;
        c.lineWidth = this.thick * bw * dp;
        c.arc(centerX, centerY, radius, -Math.PI / 2, 2 * Math.PI);
        c.stroke();
        c.beginPath();
        c.strokeStyle = this.colorFg;
        c.lineWidth = this.thick * bw * dp;

        c2 = c2.getContext("2d");
        c2.beginPath();
        c2.strokeStyle = this.innerBg;
        c2.lineWidth = 50 * bw * dp;
        c2.arc(centerX, centerY, radius2, -Math.PI / 2, 2 * Math.PI);
        c2.stroke();
        c2.beginPath();
        c2.strokeStyle = this.innerFg;
        c2.lineWidth = 50 * bw * dp;

        if(this.checkStauts) {           
            if(this.progress == 0) {
                this.canvas2.style.zIndex = 0;
                c2.strokeStyle = this.innerBg;
                t.style.color = this.startColorText;
                t.style.fontWeight = "normal";
            }
            else if(this.progress == 1) {
                this.canvas2.style.zIndex = 1;
                c2.strokeStyle = this.endInnerBg;
                t.style.color = this.endColorText;
                t.style.fontWeight = "";
            }
            else {
                this.canvas2.style.zIndex = 0;
                c2.strokeStyle = this.innerFg;
                t.style.color = this.colorText;
                t.style.fontWeight = "";
            }
        }

        if (this.round)
            c.lineCap = "round";
        
        if (this.indeterminate) {
            this.indetA = (this.indetA + 0.07 * this.animationSpeed) % (2 * Math.PI);
            this.indetB = (this.indetB + 0.14 * this.animationSpeed) % (2 * Math.PI);
            
            c.arc(centerX, centerY, radius, this.indetA, this.indetB);
            c2.arc(centerX, centerY, radius2, this.indetA, this.indetB);
            
            if (!this.noPercentage)
                this.text.innerHTML = "";
        } else {
            if (this.spin && !this.noAnimations)
                this.rot = (this.rot + 0.07 * this.animationSpeed) % (2 * Math.PI)
            
            c.arc(centerX, centerY, radius, this.rot - Math.PI / 2, this.rot + this.aniP * (2 * Math.PI) - Math.PI / 2);
            c2.arc(centerX, centerY, radius2, this.rot - Math.PI / 2, this.rot + this.aniP * (2 * Math.PI) - Math.PI / 2);
            
            if (!this.noPercentage)
                this.text.innerHTML = Math.round(100 * this.aniP) + " %";
        }

        c.stroke();
        c2.stroke();

        this.prevW = c.width;
        this.prevH = c.height;
        this.prevP = this.aniP;


        if(this.thick == 50) {            
            c.canvas.style.top = "4px";
            c.canvas.style.left = "4px";
            c.canvas.style.width = "62px";
            c.canvas.style.height = "62px";
        }
    }
    .bind(this);
    this.draw();
}
RadialProgress.prototype = {
    constructor: RadialProgress,
    setValue: function(p) {
        // this.progress = p < 0 ? 0 : p > 1 ? 1 : p;
        this.draw(0, p);
    },
    setIndeterminate: function(i) {
        this.indeterminate = i;
    },
    setText: function(t) {
        this.text.innerHTML = t;
    }
}
