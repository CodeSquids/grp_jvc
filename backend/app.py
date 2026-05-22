from flask import Flask, request, jsonify
from flask_cors import CORS
from models.visiteur import VisiteurModel
from models.visiter import VisiterModel
from visiteur_routes import visiteur_bp
from visiter_routes import visiter_bp

app = Flask(__name__)
CORS(app)  # Permet les requêtes depuis Java Swing

visiteur_model = VisiteurModel()
visiter_model = VisiterModel()

# Enregistrement des blueprints
app.register_blueprint(visiteur_bp)
app.register_blueprint(visiter_bp)

if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5000)