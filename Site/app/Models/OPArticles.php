<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $OP_A_id
 * @property int        $OP_Pr_id
 * @property Date       $OP_A_date
 * @property string     $OP_A_title
 * @property string     $OP_A_body
 * @property string     $OP_A_file
 * @property int        $X_User_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class OPArticles extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'OP_Articles';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'OP_A_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'OP_A_id', 'OP_Pr_id', 'OP_A_date', 'OP_A_title', 'OP_A_body', 'OP_A_file', 'X_User_id', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'OP_A_id' => 'int', 'OP_Pr_id' => 'int', 'OP_A_date' => 'date', 'OP_A_title' => 'string', 'OP_A_body' => 'string', 'OP_A_file' => 'string', 'X_User_id' => 'int', 'created_at' => 'datetime', 'updated_at' => 'datetime', 'deleted_at' => 'datetime'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'OP_A_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->updated_at = now();
            self::FunctionName();
        });
    }

    public static function FunctionName()
    {
        # code...
        return 1;
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_article()
    {
        return $this->belongsTo(OPProc::class, 'OP_Pr_id');
    }
    public function eq_user()
    {
        return $this->belongsTo(User::class, 'id', 'X_User_id');
    }
}
