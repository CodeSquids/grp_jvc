from flask import Flask, request, jsonify
from flask_cors import CORS
from models.visiteur import VisiteurModel
from models.visiter import VisiterModel
from models.site import SiteModel
from visiteur_routes import visiteur_bp
from visiter_routes import visiter_bp
from site_routes import site_bp

app = Flask(__name__)
CORS(app)  # Permet les requêtes depuis Java Swing

visiteur_model = VisiteurModel()
visiter_model = VisiterModel()
site_model = SiteModel()

# Enregistrement des blueprints
app.register_blueprint(visiteur_bp)
app.register_blueprint(visiter_bp)
app.register_blueprint(site_bp)

if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5000)